package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.MenuRegisterDto;
import com.moocafe.project.dto.MenuWrapperDto;
import com.moocafe.project.repository.InventoryItemRepository;
import com.moocafe.project.repository.MenuRepository;
import com.moocafe.project.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MenuController {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public MenuController(MenuService menuService, MenuRepository menuRepository, InventoryItemRepository inventoryItemRepository)
    {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

//    @GetMapping("/headOffice/menuRegister")
//    public String showForm(Model model) {
//        model.addAttribute("menuWrapper", new MenuRegisterDto());
//        return "headOffice/menuRegister";
//    }

    @PostMapping("/headOffice/menuRegister")
    public String process(@ModelAttribute MenuRegisterDto dto) {
        menuService.registerMenuWithItems(dto);
        return "redirect:/headOffice/menuRegister";
    }

    @GetMapping("/headOffice/checkMenuId")
    @ResponseBody
    public String checkMenuIdDuplicate(@RequestParam String menuId) {
        // 공백 제거 필수
        String trimmed = menuId != null ? menuId.trim() : "";
        boolean exists = menuRepository.existsByMenuId(trimmed);

        return exists ? "duplicate" : "available";
    }

    @GetMapping("/headOffice/itemName")
    @ResponseBody
    public String getItemName(@RequestParam String itemCode) {
        return inventoryItemRepository.findByItemCode(itemCode)
                .map(item -> item.getItemName())
                .orElse("");
    }

    @GetMapping("/headOffice/menuRegister")
    public String showForm(Model model) {
        model.addAttribute("menuWrapper", new MenuWrapperDto());
        model.addAttribute("itemList", inventoryItemRepository.findAll());
        return "headOffice/menuRegister";
    }

}
