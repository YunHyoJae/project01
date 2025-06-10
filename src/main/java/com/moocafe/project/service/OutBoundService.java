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
import java.util.stream.Collectors;

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
        //return outBoundRepository.findAllOutBoundDtos();
        List<Object[]> results=outBoundRepository.findAllOutBoundDtosNative();
        System.out.println(results);
        return results.stream().map(obj -> new OutBoundListResponseDto(
                (Integer) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (Integer) obj[4],
                (String) obj[5],
                (String) obj[6],
                (String) obj[7]
        )).collect(Collectors.toList());
    }
}

