package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.dto.ItemSearchDto;
import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.service.InventoryItemService;
import com.moocafe.project.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final InventoryItemService inventoryItemService;

    @GetMapping("/purchaseOrder")
    public String showForm(Model model, HttpServletRequest request) {
        List<PurchaseItemDto> emptyItems = IntStream.range(0, 5)
                .mapToObj(i -> new PurchaseItemDto())
                .collect(Collectors.toList());

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setItems(emptyItems);

        String purchaseNumber = generatePurchaseNumber();
        purchaseDto.setPurchaseNumber(purchaseNumber);

        model.addAttribute("purchaseDto", purchaseDto);
        return "headOffice/purchaseOrder";
    }


    private String generatePurchaseNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "PO" + date + "-" + random;
    }


    @PostMapping("/purchaseOrder")
    public String purchaseOrder(@ModelAttribute PurchaseDto purchaseDto) {
        purchaseService.savePurchase(purchaseDto);
        purchaseService.updateInventoryByPurchase(purchaseDto.getPurchaseNumber());
        return "redirect:/headOffice/purchaseOrder";
    }

    @GetMapping("/inventory-items/all")
    @ResponseBody
    public List<ItemSearchDto> getAllItems() {
        return purchaseService.getAllItems();
    }

    @GetMapping("/inventory-items")
    @ResponseBody
    public List<ItemSearchDto> searchItems(
            @RequestParam String type,
            @RequestParam String keyword
    ) {
        if (type.equals("name")) {
            return purchaseService.searchByName(keyword);
        } else if (type.equals("code")) {
            return purchaseService.searchByCode(keyword);
        }
        return List.of(); // 기본 빈 목록
    }


}
