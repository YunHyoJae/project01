package com.moocafe.project.dao;

import com.moocafe.project.dto.FranchiseBoardSaveDto;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FranchiseDao {
    private final FranchiseRepository fr;
    public int save(FranchiseBoard entity) {
        FranchiseBoard savedEntity = fr.save(entity);
        return (savedEntity.getId() != null) ? 1 : 0;
    }
    public List<FranchiseBoard> list() {
        return fr.findAll();
    }
    public Optional<FranchiseBoard> findById(int id) {
        return fr.findById(id);
    }
    public Page<FranchiseBoard> findAll(Pageable pageable) {return fr.findAll(pageable);}
    public Page<FranchiseBoard> findByName(String keyword, Pageable pageable) {return fr.findByNameContaining(keyword, pageable);}
    public Page<FranchiseBoard> findByTel(String keyword, Pageable pageable) {return fr.findByTelContaining(keyword, pageable);}
    public Page<FranchiseBoard> findByState(String keyword, Pageable pageable) {return fr.findByState(keyword, pageable);}
}
