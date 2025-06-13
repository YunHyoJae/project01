package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.PurchaseDto;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.service.ItemSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class ItemSearchController {
    private final ItemSearchService itemSearchService;

    @GetMapping("/itemSearchPopup")
    public String ItemSearch(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false, defaultValue = "code") String type,
                             Model model) {
        List<InventoryItem> itemList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (type.equals("code")) {
                itemList = itemSearchService.searchCode(keyword);
            } else {
                itemList = itemSearchService.searchItems(keyword);
            }
        }
        else {
            itemList = itemSearchService.getAllItems();
        }
        model.addAttribute("purchaseDto", new PurchaseDto());
        model.addAttribute("itemList", itemList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("code", type);
        return "/headOffice/itemSearchPopup";
    }
}
