package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.InboundDto;
import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.service.PurchaseService;
import com.moocafe.project.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/headOffice")
@Slf4j
public class InBoundController {
    private final PurchaseService purchaseService;
    private final ReturnService returnService;
    private final InventoryItemRepository inventoryItemRepository;

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
}