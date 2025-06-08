package com.moocafe.project.controller.itemSearch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/itemSearch")
public class ItemSearchPopup {
    @GetMapping("/itemSearchPopup")
    public String ItemSearch() {
        return "itemSearch/itemSearchPopup";
    }
}
