package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.InboundDto;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.PurchaseItemRepository;
import com.moocafe.project.repository.PurchaseRepository;
import com.moocafe.project.service.PurchaseService;
import com.moocafe.project.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/headOffice")
@Slf4j
public class InBoundController {
    private final PurchaseService purchaseService;
    private final ReturnService returnService;
    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseRepository purchaseRepository;

    @GetMapping("/inboundList")
    public String inboundList(Model model) {

        // 주문 전체(발주) 리스트
        List<Purchase> purchaseList = purchaseService.findAll();
        List<InboundDto> receiveList = new ArrayList<>();

        for (Purchase purchase : purchaseList) {
            List<PurchaseItem> items = purchase.getItems();
            for (PurchaseItem item : items) {
                // item.getItemCode()가 InventoryItem임을 전제
                InboundDto dto = InboundDto.builder()
                        .number(purchase.getPurchaseNumber())
                        .typeOrder(purchase.getTypeOrder())
                        .itemCode(item.getItemCode().getItemCode())
                        .itemName(item.getItemCode().getItemName())
                        .receivedQuantity(item.getReceivedQuantity())
                        .status(item.getStatus())
                        .build();
                receiveList.add(dto);
            }
        }

        // 매장반품조회용 리스트
        List<Return> returnList = returnService.findAllWithItems();
        List<ReturnDto> returnDtoList = returnList.stream()
                .map(returnEntity -> {
                    ReturnDto dto = new ReturnDto();
                    if (dto.getItems() == null) {
                        dto.setItems(new ArrayList<>());
                    }
                    dto.setId(returnEntity.getId());
                    dto.setReturnNumber(returnEntity.getReturnNumber());
                    dto.setRequiredDate(returnEntity.getRequiredDate());
                    dto.setReturnNote(returnEntity.getReturnNote());
                    dto.setTypeReturn(returnEntity.getTypeReturn());
                    List<ReturnItemDto> itemDtos = returnEntity.getItems().stream()
                            .map(item -> {
                                // itemCode 꺼내기 (String이어야 함, 만약 엔티티면 .getItemCode())
                                String itemCode = item.getItem().getItemCode();
                                String itemName = item.getItem().getItemName();
                                InventoryItem product = inventoryItemRepository.findByItemCode(itemCode)
                                        .orElseThrow(() -> new RuntimeException("존재하지 않는 품목코드: " + itemCode));
                                return ReturnItemDto.builder()
                                        .id(item.getId())
                                        .returnNumber(returnEntity.getReturnNumber())
                                        .itemCode(itemCode)
                                        .itemName(itemName)
                                        .returnQuantity(item.getReturnQuantity())
                                        .status(item.getStatus())
                                        .build();
                            })
                            .filter(Objects::nonNull)
                            .toList();
                    dto.setItems(itemDtos);
                    return dto;
                }).toList();

        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("receiveList", receiveList); // 필요에 따라
        model.addAttribute("returnList", returnDtoList);

        log.info("receive list : {}", receiveList);
        log.info("조회된 purchase 수: " + purchaseList.size());
        log.info("조회된 return 수: " + returnList.size());

        return "headOffice/inboundList";
    }

    @GetMapping("/order-detail")
    @ResponseBody
    public List<PurchaseItemDto> getOrderDetail(@RequestParam String purchaseNumber) {
        return purchaseService.findItemsByPurchaseNumber(purchaseNumber);
    }


    @PostMapping("/mark-complete")
    @ResponseBody
    public ResponseEntity<String> markItemAsComplete(@RequestBody Map<String, String> request) {
        String purchaseNumberStr = request.get("purchaseNumber");
        String itemCodeStr = request.get("itemCode");

        Purchase purchase = purchaseRepository.findByPurchaseNumber(purchaseNumberStr)
                .orElseThrow(() -> new RuntimeException("해당 발주가 없습니다: " + purchaseNumberStr));

        InventoryItem item = inventoryItemRepository.findByItemCode(itemCodeStr)
                .orElseThrow(() -> new RuntimeException("해당 품목이 없습니다: " + itemCodeStr));

        purchaseService.updateStatusToComplete(purchase, item);

        return ResponseEntity.ok("OK");
    }


    @PostMapping("/returnComplete")
    @ResponseBody
    public String completeReturn(@RequestBody Map<String, String> payload) {
        String returnNumber = payload.get("returnNumber");
        String itemCode = payload.get("itemCode");

        if (returnNumber == null || itemCode == null) {
            throw new IllegalArgumentException("반품번호 또는 품목코드 누락");
        }

        returnService.completeItem(returnNumber, itemCode);
        return "ok";
    }

}