package com.moocafe.project.controller;

import com.moocafe.project.service.MemberStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    public final MemberStoreService memberStoreService;
    @GetMapping("/userId/{userId}")
    public Map<String,Object> userId(@PathVariable String userId) {
        Map<String,Object> map = new HashMap<>();
        String findStore = memberStoreService.findUserId(userId);
        if(findStore != null) {
            map.put("success",true);
        }else{
            map.put("success",false);
        }
        return map;
    }
}
