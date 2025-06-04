package com.moocafe.project.controller.inventory;

import com.moocafe.project.dto.InventoryItemDto;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemController(InventoryItemService inventoryItemService,
                                   InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemService = inventoryItemService;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping("/inventory/register")
    public String showForm(Model model) {
        model.addAttribute("inventoryItem", new InventoryItemDto()); // 또는 entity 객체
        return "inventory/registerForm";  // templates/inventory/registerForm.html
    }

    @RequestMapping(value = "/inventory/register", method = RequestMethod.POST)
    public String registerItem(@ModelAttribute InventoryItemDto item, Model model) {
        boolean result = inventoryItemService.registerItem(item);
        model.addAttribute("message", result ? "등록 성공" : "등록 실패");
        return "inventory/registerForm";
    }

    @GetMapping("/check-code")
    @ResponseBody
    public Map<String, Boolean> checkItemCode(@RequestParam String itemCode) {
        boolean exists = inventoryItemRepository.existsByItemCode(itemCode);
        return Map.of("exists", exists);
    }
}
