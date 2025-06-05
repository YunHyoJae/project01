package com.moocafe.project.controller.purchase;

import com.moocafe.project.dto.PurchaseItemDto;
import com.moocafe.project.entity.PurchaseItem;
import com.moocafe.project.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping("/purchaseOrder")
    public String order() {
        return "purchase/purchaseOrder";
    }

    @PostMapping("/purchaseOrder")
    public String purchaseOrder(@ModelAttribute PurchaseItemDto purchaseItemDto) {
        purchaseService.savePurchase(purchaseItemDto);
        return "purchase/purchaseOrder";
    }

}
