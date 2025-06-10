package com.moocafe.project.service;

import com.moocafe.project.dao.OutBoundDao;
import com.moocafe.project.dao.OutBoundItemDao;
import com.moocafe.project.dto.OutBoundListResponseDto;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.OutBoundItem;
import com.moocafe.project.repository.OutBoundRepository;
import com.moocafe.project.repository.StoreOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutBoundService {

    private final OutBoundDao outBoundDao;
    private final OutBoundItemDao outBoundItemDao;
    private final OutBoundRepository outBoundRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;

    @Transactional
    public void completeOutBound(Integer outBoundId, String status) {
        OutBound origin = outBoundDao.findById(outBoundId)
                .orElseThrow(() -> new IllegalArgumentException("출고 정보를 찾을 수 없습니다."));

        OutBound updated = new OutBound(
                origin.getOutBoundId(),
                origin.getStoreId(),
                origin.getRequiredDate(),
                origin.getApproved(),
                origin.getApprovedDate(),
                new Date(),
                status
        );

        outBoundDao.save(updated);

        String newOrderStatus = status.equals("출고완료") ? "출고완료" : "준비중";

        List<OutBoundItem> items = outBoundItemDao.findByOutBoundId(outBoundId);
        for (OutBoundItem item : items) {
            storeOrderDetailRepository.updateStatusByStoreAndItem(
                    origin.getStoreId(),
                    item.getItemCode(),
                    newOrderStatus
            );
        }
    }
    public List<OutBoundListResponseDto> getOutBoundList() {
        return outBoundRepository.findAllOutBoundDtos();
    }
}

