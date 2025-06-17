package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.MenuRegisterDto;
import com.moocafe.project.dto.MenuWrapperDto;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.MenuRepository;
import com.moocafe.project.service.InventoryItemService;
import com.moocafe.project.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final InventoryItemService inventoryItemService;

    @PostMapping("/headOffice/menuRegister")
    public String process(@ModelAttribute MenuRegisterDto dto) {
        menuService.registerMenuWithItems(dto);
        return "redirect:/headOffice/menuRegister";
    }

    @GetMapping("/headOffice/checkMenuId")
    @ResponseBody
    public String checkMenuIdDuplicate(@RequestParam String menuId) {
        String trimmed = menuId != null ? menuId.trim() : "";
        boolean exists = menuService.existsByMenuId(trimmed);

        return exists ? "duplicate" : "available";
    }

    @GetMapping("/headOffice/itemName")
    @ResponseBody
    public String getItemName(@RequestParam String itemCode) {
        return inventoryItemService.findItemNameByItemCode(itemCode);
    }

    @GetMapping("/headOffice/menuRegister")
    public String showForm(Model model) {
        model.addAttribute("menuWrapper", new MenuWrapperDto());
        model.addAttribute("itemList", inventoryItemService.findAll());
        return "headOffice/menuRegister";
    }

}
