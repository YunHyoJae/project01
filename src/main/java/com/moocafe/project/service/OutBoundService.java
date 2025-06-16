package com.moocafe.project.service;

import com.moocafe.project.dao.OutBoundDao;
import com.moocafe.project.dao.OutBoundItemDao;
import com.moocafe.project.dto.OutBoundListResponseDto;
import com.moocafe.project.entity.InventoryStore;
import com.moocafe.project.entity.OutBound;
import com.moocafe.project.entity.OutBoundItem;
import com.moocafe.project.repository.InventoryStoreRepository;
import com.moocafe.project.repository.OutBoundRepository;
import com.moocafe.project.repository.StoreOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final InventoryStoreRepository inventoryStoreRepository;

    @Transactional
    public void completeOutBound(Integer outBoundId, String status) {
        OutBound origin = outBoundDao.findById(outBoundId)
                .orElseThrow(() -> new IllegalArgumentException("출고 정보를 찾을 수 없습니다."));

        Date now = new Date();

        Date requiredDate = origin.getRequiredDate();
        Date approvedDate = origin.getApprovedDate();
        Date dueDate = origin.getDueDate();

        if ("준비중".equals(status)) {
            requiredDate = now;
            approvedDate = now;
            dueDate = null;
        } else if ("출고완료".equals(status)) {
            if (requiredDate == null) requiredDate = now;
            if (approvedDate == null) approvedDate = now;
            dueDate = now;
        }

        OutBound updated = new OutBound(
                origin.getOutBoundId(),
                origin.getStoreId(),
                requiredDate,
                'Y',
                approvedDate,
                dueDate,
                status
        );
        outBoundDao.save(updated);

        String newOrderStatus = status.equals("출고완료") ? "출고완료" : "준비중";

        List<OutBoundItem> items = outBoundItemDao.findByOutBoundId(outBoundId);
        for (OutBoundItem item : items) {
            String itemCode = item.getItemCode();
            int qty = item.getReceivedQuantity();

            storeOrderDetailRepository.updateStatusByStoreAndItem(
                    origin.getStoreId(),
                    itemCode,
                    newOrderStatus
            );

            if ("출고완료".equals(status)) {
                inventoryStoreRepository.decreaseStock(1, itemCode, qty);

                List<InventoryStore> storeStockList = inventoryStoreRepository
                        .findByItemCodeAndStoreId(itemCode, origin.getStoreId());

                InventoryStore storeInventory;
                if (storeStockList.isEmpty()) {
                    storeInventory = new InventoryStore(
                            itemCode,
                            origin.getStoreId(),
                            qty,
                            now
                    );
                } else {
                    storeInventory = storeStockList.get(0);
                    storeInventory.updateCount(storeInventory.getCount() + qty, now);
                }

                inventoryStoreRepository.save(storeInventory);
            }
        }
    }

//    public List<OutBoundListResponseDto> getOutBoundList() {
//        List<Object[]> results = outBoundRepository.findAllOutBoundDtosNative();
//        return results.stream().map(obj -> new OutBoundListResponseDto(
//                (Integer) obj[0],
//                (String) obj[1],
//                (String) obj[2],
//                (String) obj[3],
//                (Integer) obj[4],
//                (String) obj[5],
//                (String) obj[6],
//                (String) obj[7]
//        )).collect(Collectors.toList());
//    }

    public Page<OutBoundListResponseDto> getOutBoundListWithConditions(
            String startDate, String endDate, String storeName, Pageable pageable) {

        Page<Object[]> result = outBoundRepository.findOutBoundsByConditionNativePageable(startDate, endDate, storeName, pageable);

        return result.map(obj -> new OutBoundListResponseDto(
                (Integer) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                (Integer) obj[4],
                (String) obj[5],
                (String) obj[6],
                (String) obj[7]
        ));
    }
}


