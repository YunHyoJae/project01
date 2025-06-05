package com.moocafe.project.dao;

import com.moocafe.project.dto.FranchiseBoardSaveDto;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FranchiseDao {
    private final FranchiseRepository fr;
    public int save(FranchiseBoardSaveDto dto) {
        FranchiseBoard entity = FranchiseBoardSaveDto.toEntity(dto);
        FranchiseBoard savedEntity = fr.save(entity);
        return (savedEntity.getId() != null) ? 1 : 0;
    }
    public List<FranchiseBoard> list() {
        return fr.findAll();
    }
}
