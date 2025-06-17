package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @GetMapping("/headOffice/inventoryRegister")
    public String showForm(Model model) {
        if (!model.containsAttribute("inventoryItem")) {
            model.addAttribute("inventoryItem", new InventoryItemDto());
        }
        return "headOffice/inventoryRegister";
    }

    @PostMapping("/headOffice/inventoryRegister")
    public String registerItem(@ModelAttribute InventoryItemDto item, RedirectAttributes redirectAttributes) {
        System.out.println("등록 요청됨: " + item.getItemCode() + ", 유통기한: " + item.getExpirationDate());
        boolean result = inventoryItemService.registerItem(item);
        redirectAttributes.addFlashAttribute("message", result ? "등록 성공" : "등록 실패");
        return "redirect:/headOffice/inventoryRegister";
    }

    @GetMapping("/headOffice/check-code")
    @ResponseBody
    public Map<String, Boolean> checkItemCode(@RequestParam String itemCode) {
        boolean exists = inventoryItemService
                .isItemCodeExists(itemCode);
        return Map.of("exists", exists);
    }
}
