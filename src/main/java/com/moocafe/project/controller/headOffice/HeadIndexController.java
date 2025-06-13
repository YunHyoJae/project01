package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.MenuRepository;
import com.moocafe.project.repository.SalesRepository;
import com.moocafe.project.repository.StoreRepository;
import com.moocafe.project.service.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadIndexController {
    private final SalesRepository salesRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final FranchiseService franchiseService;

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
        model.addAttribute("shortageList", shortageList);
        model.addAttribute("user", user.toDto());

        List<FranchiseBoardDto> alarmList = franchiseService.listByState("상담신청");
        model.addAttribute("alarmList", alarmList);


        return "headOffice/index";
    }
}