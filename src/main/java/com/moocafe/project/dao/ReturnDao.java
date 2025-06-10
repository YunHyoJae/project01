package com.moocafe.project.dao;

import com.moocafe.project.entity.Return;
import com.moocafe.project.repository.ReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReturnDao {
    private final ReturnRepository returnRepository;

    public void saveReturn(Return returnEntity) {
        returnRepository.save(returnEntity);
    }
}
