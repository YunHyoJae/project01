package com.moocafe.project.controller;

import com.moocafe.project.service.MemberStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    public final MemberStoreService memberStoreService;
    @GetMapping("/userId/{userId}")
    public Map<String,Object> userId(@PathVariable String userId) {
        Map<String,Object> map = new HashMap<>();
        String findStore = memberStoreService.findUserId(userId);
        if(findStore != null) {
            map.put("success",false);
        }else{
            map.put("success",true);
        }
        return map;
    }
    @GetMapping("/storeNumber/{storeNumber}")
    public Map<String,Object> storeNumber(@PathVariable String storeNumber) {
        Map<String,Object> map = new HashMap<>();
        String findStore = memberStoreService.findByStoreNumber(storeNumber);
        if(findStore != null) {
            map.put("success",false);
        }else{
            map.put("success",true);
        }
        return map;
    }
    
}
