package com.moocafe.project.service;

import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dao.StoreOrderDao;
import com.moocafe.project.dao.StoreOrderDetailDao;
import com.moocafe.project.dto.StoreOrderDetailDto;
import com.moocafe.project.dto.StoreOrderDto;
import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.Store;
import com.moocafe.project.entity.StoreOrder;
import com.moocafe.project.entity.StoreOrderDetail;
import com.moocafe.project.entity.StoreOrderDetailId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderService {

    private final StoreDao storeDao;
    private final StoreOrderDao storeOrderDao;
    private final StoreOrderDetailDao storeOrderDetailDao;

    @Transactional
    public void createOrder(StoreOrderDto orderDto) {
        Store store = storeDao.findById(orderDto.getStoreId().intValue())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다"));
        StoreOrder savedOrder = storeOrderDao.save(
                StoreOrder.builder()
                        .OrderNumber(orderDto.getOrderNumber())
                        .storeId(orderDto.getStoreId())
                        .OrderDate(LocalDateTime.now())
                        .build()
        );

        for (StoreOrderDetailDto item : orderDto.getItems()) {
            StoreOrderDetailId id = new StoreOrderDetailId(savedOrder.getId(), item.getItemCode());
            StoreOrderDetail detail = StoreOrderDetail.builder()
                    .orderId(id.getOrderId())
                    .ItemCode(id.getItemCode())
                    .OrderedQuantity(item.getOrderedQuantity())
                    .status("주문완료")
                    .storeOrder(savedOrder)
                    .build();
            storeOrderDetailDao.save(detail);
        }
    }

    @Transactional(readOnly = true)
    public List<StoreOrderListResponseDto> getOrderList(Integer storeId, String startDate, String endDate) {
        List<Object[]> rawResults = storeOrderDetailDao.findOrderListByStoreAndDate(storeId, startDate, endDate);
        return rawResults.stream().map(obj -> new StoreOrderListResponseDto(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3],
                ((Number) obj[4]).intValue(),
                (String) obj[5]
        )).toList();
    }
}