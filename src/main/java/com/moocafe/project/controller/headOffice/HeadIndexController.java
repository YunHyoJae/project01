package com.moocafe.project.controller.headOffice;

import com.moocafe.project.dto.*;
import com.moocafe.project.entity.InventoryItem;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.Store;
import com.moocafe.project.repository.*;
import com.moocafe.project.service.FranchiseService;
import com.moocafe.project.service.HeadIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/headOffice")
@RequiredArgsConstructor
public class HeadIndexController {
    private final HeadIndexService headIndexService;
    private final FranchiseService franchiseService;

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        HeadIndexDashboardDto dashboard = headIndexService.getDashboard();

        model.addAttribute("summaryList", dashboard.getSummaryList());
        model.addAttribute("pivotList", dashboard.getPivotList());
        model.addAttribute("shortageList", dashboard.getShortageList());
        model.addAttribute("recentPurchaseItems", dashboard.getRecentPurchaseItems());
        model.addAttribute("recentOutbounds", dashboard.getRecentOutbounds());
        model.addAttribute("storeIdNameMap", dashboard.getStoreIdNameMap());
        model.addAttribute("itemCodeNameMap", dashboard.getItemCodeNameMap());
        model.addAttribute("user", user.toDto());

        List<FranchiseBoardDto> alarmList = franchiseService.listByState("상담신청");
        model.addAttribute("alarmList", alarmList);

        return "headOffice/index";
    }
}