package com.moocafe.project.service;

import com.moocafe.project.dao.OutBoundDao;
import com.moocafe.project.dao.OutBoundItemDao;
import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dto.OutBoundDto;
import com.moocafe.project.dto.OutBoundItemDto;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.OutBoundItem;
import com.moocafe.project.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OutBoundService {

    private final OutBoundDao outBoundDao;
    private final OutBoundItemDao outBoundItemDao;
    private final StoreDao storeDao;

    @Transactional
    public void requestOutBound(OutBoundDto dto) {
        Store store = storeDao.findById(dto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));
        OutBound outBound = OutBound.builder()
                .RequiredDate(new Date())
                .status("준비중")
                .Approved('N')
                .storeId(store.getId()) // OutBound에 store 객체가 아니라 storeId만 있음
                .build();

        outBound = outBoundDao.save(outBound);

        for (OutBoundItemDto item : dto.getItems()) {
            OutBoundItem entity = OutBoundItem.builder()
                    .outBound(outBound)
                    .ItemCode(item.getItemCode())
                    .ReceivedQuantity(item.getReceivedQuantity())
                    .build();
            outBoundItemDao.save(entity);
        }
    }

    @Transactional
    public void updateStatus(Integer outBoundId, String status) {
        OutBound origin = outBoundDao.findById(outBoundId)
                .orElseThrow(() -> new IllegalArgumentException("출고 정보를 찾을 수 없습니다"));

        OutBound updated = OutBound.builder()
                .OutBoundId(origin.getOutBoundId())
                .storeId(origin.getStoreId())
                .RequiredDate(origin.getRequiredDate())
                .Approved(origin.getApproved())
                .ApprovedDate(origin.getApprovedDate())
                .DueDate(new Date()) // 새로 설정
                .status(status) // 새로 설정
                .build();

        outBoundDao.save(updated);
    }
}
