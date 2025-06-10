package com.moocafe.project.controller.storeOwner;

import com.moocafe.project.dto.ReturnDto;
import com.moocafe.project.dto.ReturnItemDto;
import com.moocafe.project.entity.Return;
import com.moocafe.project.entity.ReturnItem;
import com.moocafe.project.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@RequestMapping("/storeOwner")
@RequiredArgsConstructor
public class ReturnController {
    private final ReturnService returnService;

    @GetMapping("/return")
    public String showReturnForm(Model model) {
        ReturnDto returnDto = new ReturnDto();

        String returnNumber = generateReturnNumber();
        returnDto.setReturnNumber(returnNumber);
        returnDto.setRequiredDate(LocalDateTime.now());

        model.addAttribute("returnDto", returnDto);
        return "storeOwner/return";
    }

    private String generateReturnNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "RE" + date + "-" + random;
    }


    @PostMapping("/return")
    public String returnPost(@ModelAttribute ReturnDto returnEntityDto) {
        returnService.saveReturn(returnEntityDto);
        return "storeOwner/return";
    }
}
