package com.moocafe.project.dao;

import com.moocafe.project.entity.Return;
import com.moocafe.project.repository.ReturnItemRepository;
import com.moocafe.project.repository.ReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReturnDao {
    private final ReturnRepository returnRepository;
    private final ReturnItemRepository returnItemRepository;

    public Return saveReturn(Return returnEntity) {
        return returnRepository.save(returnEntity);
    }

}
