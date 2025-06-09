package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/purchaseOrder")
    public String showForm(Model model) {
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
        return "redirect:/headOffice/purchaseOrder";
    }

}
