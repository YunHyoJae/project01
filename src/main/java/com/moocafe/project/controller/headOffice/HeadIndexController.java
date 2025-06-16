package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadIndexController {
    private final SalesRepository salesRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final FranchiseService franchiseService;
    private final OutBoundRepository outBoundRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        // summaryList 추가 (매출 차트용)
        List<SalesSummaryDto> summaryList = salesRepository.findSalesSummary(null, null, null);
        model.addAttribute("summaryList", summaryList);

        // ----- 🔥 [1] pivotList(재고 요약) 생성 -----
        List<InventorySummaryDto> rawList = inventoryStoreRepository.getInventorySummary();
        Map<String, InventorySummaryPivotRowDto> pivotMap = new LinkedHashMap<>();

        // 3개월 예상 사용량 계산용 날짜 범위
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
            List<Menu> menus = menuRepository.findByItemCode(dto.getItemCode());
            int totalExpectedUsage = 0;
            for (Menu menu : menus) {
                int usedQty = menu.getQuantityUsed();
                int soldQty = salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(
                        dto.getStoreId(), menu.getMenuId(), startDate, endDate
                );
                totalExpectedUsage += usedQty * soldQty;
            }
            row.putExpectedUsage(dto.getStoreId(), totalExpectedUsage);
        }

        List<InventorySummaryPivotRowDto> pivotList = new ArrayList<>(pivotMap.values());
        List<Store> storeList = storeRepository.findAll();

        List<InventoryShortageDto> shortageList = new ArrayList<>();
        for (InventorySummaryPivotRowDto row : pivotList) {
            for (Store store : storeList) {
                String css = row.getStockCssClass(store.getId());
                int stock = row.getStockByStoreId(store.getId());
                if ("low-stock".equals(css)) {
                    shortageList.add(new InventoryShortageDto(
                            row.getItemName(),
                            store.getName(),
                            stock
                    ));
                }
            }
        }

        // 출고리스트에서 최근 4개 가져오기 (status, 정렬 등 조건 필요시 조정)
        List<Store> stores = storeRepository.findAll();
        Map<Integer, String> storeIdNameMap = stores.stream()
                .collect(Collectors.toMap(Store::getId, Store::getName));

        List<InventoryItem> items = inventoryItemRepository.findAll();
        Map<String, String> itemCodeNameMap = items.stream()
                .collect(Collectors.toMap(InventoryItem::getItemCode, InventoryItem::getItemName));

        List<Object[]> outBoundRawList = outBoundRepository.findRecentOutBoundListRaw();
        List<OutBoundListResponseDto> recentOutbounds = outBoundRawList.stream()
                .map(arr -> new OutBoundListResponseDto(
                        (Integer) arr[0],                  // outBoundId
                        (String) arr[1],                   // storeName
                        (String) arr[2],                   // itemCode
                        (String) arr[3],                   // itemName
                        arr[4] != null ? ((Number) arr[4]).intValue() : null, // receivedQuantity
                        (String) arr[5],                   // requiredDate
                        (String) arr[6],                   // dueDate
                        (String) arr[7]                    // status
                ))
                .collect(Collectors.toList());

        // 입고리스트에서 최근 4개 가져오자

        List<Object[]> recentItems = purchaseItemRepository.findRecent4();
        List<HeadPurchaseItemDto> list = recentItems.stream()
                .map(arr -> new HeadPurchaseItemDto(
                        arr[0] == null ? null : Integer.valueOf(arr[0].toString()), // id
                        arr[1] == null ? null : arr[1].toString(), // itemCode
                        arr[2] == null ? null : arr[2].toString(), // itemName
                        arr[3] == null ? null : Integer.valueOf(arr[3].toString()), // receivedQuantity
                        arr[4] == null ? null : ((Date) arr[4]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), // dueDate
                        arr[5] == null ? null : ((Date) arr[5]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), // expirationDate
                        arr[6] == null ? null : arr[6].toString(), // supplier
                        arr[7] == null ? null : arr[7].toString()  // status
                ))
                .collect(Collectors.toList());

        model.addAttribute("recentPurchaseItems", list);

        model.addAttribute("recentOutbounds", recentOutbounds);
        model.addAttribute("storeIdNameMap", storeIdNameMap);
        model.addAttribute("itemCodeNameMap", itemCodeNameMap);
        model.addAttribute("shortageList", shortageList);
        model.addAttribute("user", user.toDto());

        List<FranchiseBoardDto> alarmList = franchiseService.listByState("상담신청");
        model.addAttribute("alarmList", alarmList);


        return "headOffice/index";
    }
}