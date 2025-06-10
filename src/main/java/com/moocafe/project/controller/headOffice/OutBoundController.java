package com.moocafe.project.controller.headOffice;


import com.moocafe.project.dto.OutBoundListResponseDto;
import com.moocafe.project.dto.OutBoundStatusDto;
import com.moocafe.project.service.OutBoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/headoffice")
public class OutBoundController {

    private final OutBoundService outBoundService;

    @GetMapping("/outbound/list")
    public String showOutBoundList(Model model) {
        List<OutBoundListResponseDto> list = outBoundService.getOutBoundList();
        model.addAttribute("outbounds", list);
        return "headOffice/outbound-list";
    }

    @PostMapping("/outbound/update-status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@RequestBody OutBoundStatusDto dto) {
        if (dto.getOutBoundIds() == null || dto.getOutBoundIds().isEmpty()) {
            return ResponseEntity.badRequest().body("출고 ID가 누락되었습니다.");
        }

        String status = dto.getStatus();

        try {
            for (Integer id : dto.getOutBoundIds()) {
                if ("승인".equals(status)) {
                    outBoundService.completeOutBound(id, "준비중");
                } else if ("출고완료".equals(status)) {
                    outBoundService.completeOutBound(id, "출고완료");
                } else {
                    return ResponseEntity.badRequest().body("유효하지 않은 상태입니다.");
                }
            }
            return ResponseEntity.ok("상태 변경이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류: 상태 변경 실패");
        }
    }
    @GetMapping("/update-status")
    public String showUpdateStatusPage() {
        return "headOffice/update-status";
    }
}
