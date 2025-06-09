package com.moocafe.project.service;

import com.moocafe.project.dao.ReturnDao;
import com.moocafe.project.entity.Return;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnService {
    private final ReturnDao returnDao;

    public void saveReturn(Return returnEntity) {
        returnDao.saveReturn(returnEntity);
    }
}
