package com.moocafe.project.service;

import com.moocafe.project.dao.FranchiseDao;
import com.moocafe.project.dto.FranchiseBoardDto;
import com.moocafe.project.dto.FranchiseBoardSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseDao dao;
    public int save(FranchiseBoardSaveDto dto){
        return dao.save(dto);
    }
    public List<FranchiseBoardDto> list(){
        return dao.list().stream().map(FranchiseBoardDto::toDto).toList();
    }
}
