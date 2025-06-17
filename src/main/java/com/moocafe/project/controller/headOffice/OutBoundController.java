package com.moocafe.project.controller.headOffice;


import com.moocafe.project.dto.OutBoundListResponseDto;
import com.moocafe.project.dto.OutBoundStatusDto;
import com.moocafe.project.service.OutBoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/headOffice")
public class OutBoundController {

    private final OutBoundService outBoundService;

    @GetMapping("/outboundManagement")
    public String showOutBoundList(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String storeName,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.minusYears(1).toString();
            endDate = now.toString();
        }

        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, 10);
        Page<OutBoundListResponseDto> pageResult =
                outBoundService.getOutBoundListWithConditions(startDate, endDate, storeName, pageable);

        model.addAttribute("outbounds", pageResult.getContent());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("storeName", storeName);
        model.addAttribute("currentPage", safePage);
        model.addAttribute("totalPages", pageResult.getTotalPages());

        return "headOffice/outboundManagement";
    }

    @PostMapping("/outboundManagement")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody OutBoundStatusDto dto) {
        if (dto.getOutBoundIds() == null || dto.getOutBoundIds().isEmpty()) {
            return ResponseEntity.badRequest().body("출고 ID가 누락되었습니다.");
        }

        String status = dto.getStatus();

        try {
            for (Integer id : dto.getOutBoundIds()) {
                if ("승인".equals(status)) {
                    outBoundService.completeOutBound(id, "출고준비중");
                } else if ("출고완료".equals(status)) {
                    outBoundService.completeOutBound(id, "출고완료");
                } else {
                    return ResponseEntity.badRequest().body("유효하지 않은 상태입니다.");
                }
            }
            return ResponseEntity.ok("상태 변경이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("서버 오류: 상태 변경 실패");
        }
    }
}
