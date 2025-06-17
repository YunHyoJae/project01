package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storeOwner")
public class StoreIndexController {
    private final SalesService salesService;
    private final MemberStoreService memberStoreService;
    private final InventoryStoreService inventoryStoreService;
    private final MenuService menuService;
    private final StoreOrderService storeOrderService;

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("user", user.toDto());

        Member loginMember = user.getLoggedMember();
        List<MemberStore> msList = memberStoreService.findByMemberId(loginMember.getId());

        if (!msList.isEmpty()) {
            Store store = msList.get(0).getStore();
            Integer storeId = store.getId();

            // 1. 메뉴별 최근 3개월 매출 (Stacked Bar Chart용)
            LocalDate now = LocalDate.now();
            List<String> chartLabels = new ArrayList<>(); // x축: 최근 3개월 ["4월", "5월", "6월"]
            List<LocalDate> monthStarts = new ArrayList<>();
            for (int i = 2; i >= 0; i--) {
                LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
                chartLabels.add(monthStart.getMonthValue() + "월");
                monthStarts.add(monthStart);
            }

            // 메뉴 종류 추출 (3개월간 등장한 메뉴를 모두 포함)
            Set<String> menuNamesSet = new LinkedHashSet<>();
            List<List<SalesSummaryDto>> salesByMonth = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                LocalDate firstDay = monthStarts.get(i);
                LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                List<SalesSummaryDto> monthly = salesService.getSalesSummary(storeId, firstDay, lastDay);
                salesByMonth.add(monthly);
                for (SalesSummaryDto dto : monthly) menuNamesSet.add(dto.getMenuName());
            }
            List<String> menuNames = new ArrayList<>(menuNamesSet);

            // 메뉴별 월별 매출 데이터 만들기
            Map<String, List<Integer>> menuSales = new LinkedHashMap<>();
            for (String menu : menuNames) menuSales.put(menu, Arrays.asList(0, 0, 0));

            // ② 합산 방식으로 변경!
            for (int m = 0; m < 3; m++) {
                for (SalesSummaryDto dto : salesByMonth.get(m)) {
                    String menu = dto.getMenuName();
                    int amount = dto.getTotalAmount() != null ? dto.getTotalAmount().intValue() : 0;
                    // 기존: menuSales.get(menu).set(m, amount); // 이렇게 하면 마지막 값만 남음
                    // -> 누적합으로 변경
                    List<Integer> data = menuSales.get(menu);
                    data.set(m, data.get(m) + amount); // 기존값에 더함
                }
            }
            // Chart.js의 datasets 준비 (색상은 예시)
            String[] colors = {"#f06292", "#4fc3f7", "#aed581", "#ffb300", "#ba68c8", "#f57c00", "#bdbdbd"};
            List<Map<String, Object>> menuDatasets = new ArrayList<>();
            int idx = 0;
            for (String menu : menuNames) {
                Map<String, Object> ds = new HashMap<>();
                ds.put("label", menu);
                ds.put("data", menuSales.get(menu));
                ds.put("backgroundColor", colors[idx % colors.length]);
                menuDatasets.add(ds);
                idx++;
            }
            model.addAttribute("chartLabels", chartLabels);
            model.addAttribute("menuDatasets", menuDatasets);

            // 2. 부족재고 (기존 방식 유지)
            List<InventorySummaryDto> rawList = inventoryStoreService.getInventorySummary();
            Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();
            // pivot 데이터 생성
            LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
            Date startDate = java.sql.Date.valueOf(threeMonthsAgo);
            Date endDate = java.sql.Date.valueOf(LocalDate.now());
            for (InventorySummaryDto dto : rawList) {
                String key = dto.getItemCode() + "::" + dto.getItemName();
                pivotMap.putIfAbsent(key, new InventorySummaryPivotRowDto());
                InventorySummaryPivotRowDto row = pivotMap.get(key);
                row.setItemCode(dto.getItemCode());
                row.setItemName(dto.getItemName());
                row.getStoreStockMap().put(dto.getStoreId(), dto.getTotalCount().intValue());
                // 예상 사용량 계산
                List<Menu> menus = menuService.findByItemCode(dto.getItemCode());
                int totalExpectedUsage = 0;
                for (Menu menu : menus) {
                    int usedQty = menu.getQuantityUsed();
                    int soldQty = salesService.sumQuantityByStoreIdAndMenuIdAndPeriod(
                            dto.getStoreId(), menu.getMenuId(), startDate, endDate
                    );
                    totalExpectedUsage += usedQty * soldQty;
                }
                row.putExpectedUsage(dto.getStoreId(), totalExpectedUsage);
            }
            List<InventorySummaryPivotRowDto> pivotList = new ArrayList<>(pivotMap.values());
            List<InventoryShortageDto> shortageList = new ArrayList<>();
            for (InventorySummaryPivotRowDto row : pivotList) {
                String css = row.getStockCssClass(storeId); // 내 점포만
                int stock = row.getStockByStoreId(storeId);
                if ("low-stock".equals(css)) {
                    shortageList.add(new InventoryShortageDto(
                            row.getItemName(),
                            store.getName(),
                            stock
                    ));
                }
            }

            List<Object[]> orderRawList = storeOrderService.findRecentOrderListByStoreId(storeId);
            List<StoreOrderListResponseDto> orderList = orderRawList.stream()
                    .map(arr -> new StoreOrderListResponseDto(
                            (String) arr[0],
                            (String) arr[1],
                            (String) arr[2],
                            (String) arr[3],
                            arr[4] != null ? ((Number) arr[4]).intValue() : 0,
                            (String) arr[5]
                    ))
                    .collect(Collectors.toList());
            model.addAttribute("orderList", orderList);


            model.addAttribute("shortageList", shortageList);
            model.addAttribute("store", store);
        }

        return "storeOwner/index";
    }
}