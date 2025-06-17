package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.service.InventoryStoreService;
import com.moocafe.project.service.ReturnService;
import com.moocafe.project.service.StoreOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/storeOwner")
@RequiredArgsConstructor
@Slf4j
public class StoreOrderController {

    private final StoreOrderService storeOrderService;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryStoreRepository inventoryStoreRepository;
    private final ReturnService returnService;
    private final InventoryStoreService inventoryStoreService;


@GetMapping("/storeorderForm")
public String showOrderForm(
        @RequestParam(value = "itemCode", required = false) List<String> itemCodes,
        @RequestParam(value = "needed", required = false) List<Integer> neededs,
        Model model,
        @AuthenticationPrincipal CustomUserDetails userDetails)
{
    // 기존 코드
    Integer storeId = userDetails.toDto().getStoreId();
    String orderNumber = storeOrderService.generateOrderNumber(storeId);
    model.addAttribute("orderNumber", orderNumber);

    // 전달된 아이템 정보 DTO로 만들어서 모델에 담기
    List<OrderItemDto> orderItems = new ArrayList<>();
    if (itemCodes != null && neededs != null) {
        for (int i = 0; i < itemCodes.size(); i++) {
            String code = itemCodes.get(i);
            Integer needed = neededs.get(i);
            // 필요하다면 DB에서 itemName, price 조회
            orderItems.add(new OrderItemDto(code, needed));
        }
    }
    model.addAttribute("orderItems", orderItems);

    model.addAttribute("orderDto", new StoreOrderDto());
    return "/storeOwner/storeorderForm";
}



    @GetMapping("/storeorderList")
    public String showOrderListPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
            Model model
    ) {
        Integer storeId = userDetails.toDto().getStoreId();

        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.minusYears(1).toString();
            endDate = now.toString();
        }

        List<StoreOrderListResponseDto> orderList = storeOrderService.getOrderList(storeId, startDate, endDate);

        List<ReturnItemDto> items = IntStream.range(0, 3)
                .mapToObj(i -> new ReturnItemDto())
                .collect(Collectors.toList());

        ReturnDto returnDto = new ReturnDto();
        returnDto.setItems(items); //returnDto.setReturnItems(items);
        returnDto.setReturnNumber(generateReturnNumber());

        log.info("orderList: {}", orderList);

        model.addAttribute("returnDto", returnDto);
        model.addAttribute("orderList", orderList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "/storeOwner/storeorderList";
    }

    @GetMapping("/orderList")
    public String redirectToOrderListPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
            Model model
    ) {
        return showOrderListPage(userDetails, startDate, endDate, model);
    }

    private String generateReturnNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "RE" + date + "-" + random;
    }

    @PostMapping("/storeOrderReturn")
    public String submitReturn(@ModelAttribute ReturnDto returnDto, String itemCode, int returnQuantity, int storeId) {
        log.info("submit return: {}", returnDto);
        returnService.saveReturn(returnDto, itemCode, returnQuantity, storeId);
        return "redirect:/storeOwner/storeorderList";
    }
    @GetMapping("/storeOrderItems")
    @ResponseBody
    public List<ItemSearchDto> getItemList() {
        return inventoryStoreRepository.findByStoreId(1)
                .stream()
                .map(store -> {
                    InventoryItem item = inventoryItemRepository.findByItemCode(store.getItemCode())
                            .orElseThrow(() -> new IllegalArgumentException("해당 itemCode에 대한 기초 품목 정보가 없습니다: " + store.getItemCode()));
                    return new ItemSearchDto(
                            item.getItemCode(),
                            item.getItemName(),
                            item.getItemPrice(),
                            store.getCount()
                    );
                }).toList();
    }

    @PostMapping("/storeOrder")
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

    @GetMapping("/storeOrder/itemInfo")
    @ResponseBody
    public ResponseEntity<ItemSearchDto> getItemInfo(@RequestParam String itemCode) {
        Optional<InventoryItem> itemOpt = inventoryStoreService.findInventoryItemByItemCode(itemCode);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        InventoryItem item = itemOpt.get();
        int stockQty = inventoryStoreService.findQuantityByStoreIdAndItemCode(1, itemCode);

        ItemSearchDto dto = new ItemSearchDto(
                item.getItemCode(),
                item.getItemName(),
                item.getItemPrice(),
                stockQty);
        return ResponseEntity.ok(dto);
    }

//    @GetMapping("/storeOrderList")
//    @ResponseBody
//    public ResponseEntity<List<StoreOrderListResponseDto>> getOrderList(
//            @AuthenticationPrincipal CustomUserDetails userDetails,
//            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
//            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
//    ) {
//        Integer storeId = userDetails.toDto().getStoreId();
//        List<StoreOrderListResponseDto> orderList = storeOrderService.getOrderList(storeId, startDate, endDate);
//        return ResponseEntity.ok(orderList);
//    }
}