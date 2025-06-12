package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.ItemSearchDto;
import com.moocafe.project.dto.StoreOrderDto;
import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/storeOwner")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;
    private final InventoryItemRepository inventoryItemRepository;

    @GetMapping("/storeorder/form")
    public String showOrderForm(Model model) {
        model.addAttribute("orderDto", new StoreOrderDto());
        return "storeOwner/storeorder-form";
    }

    @GetMapping("/storeorder/list-view")
    public String showOrderListPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
            Model model
    ) {
        Integer storeId = userDetails.toDto().getStoreId();

        List<StoreOrderListResponseDto> orderList = storeOrderService.getOrderList(storeId, startDate, endDate);

        model.addAttribute("orderList", orderList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "storeOwner/storeorder-list";
    }

    @GetMapping("/storeorder/storeorder-popup")
    public String showItemPopup(Model model) {
        List<ItemSearchDto> items = inventoryItemRepository.findAll()
                .stream()
                .map(i -> new ItemSearchDto(
                        i.getItemCode(),
                        i.getItemName(),
                        i.getItemPrice()))
                .toList();
        model.addAttribute("items", items);
        return "storeOwner/storeorder-popup";
    }


    @PostMapping("/storeorder")
    @ResponseBody
    public ResponseEntity<String> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StoreOrderDto orderDto) {

        Integer storeId = userDetails.toDto().getStoreId();
        String orderNumber = storeOrderService.generateOrderNumber(storeId);

        orderDto.setStoreId(storeId);
        orderDto.setOrderNumber(orderNumber);

        storeOrderService.createOrder(orderDto);
        return ResponseEntity.ok("주문성공");
    }

    @GetMapping("/storeorder/list")
    @ResponseBody
    public ResponseEntity<List<StoreOrderListResponseDto>> getOrderList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        Integer storeId = userDetails.toDto().getStoreId();
        List<StoreOrderListResponseDto> orderList = storeOrderService.getOrderList(storeId, startDate, endDate);
        return ResponseEntity.ok(orderList);
    }
}
