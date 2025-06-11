package com.moocafe.project.service;

import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dao.StoreOrderDao;
import com.moocafe.project.dao.StoreOrderDetailDao;
import com.moocafe.project.dto.StoreOrderDetailDto;
import com.moocafe.project.dto.StoreOrderDto;
import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.OutBoundItemRepository;
import com.moocafe.project.repository.OutBoundRepository;
import com.moocafe.project.repository.StoreOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderService {

    private final StoreDao storeDao;
    private final StoreOrderDao storeOrderDao;
    private final StoreOrderDetailDao storeOrderDetailDao;
    private final StoreOrderRepository storeOrderRepository;
    private final OutBoundRepository outBoundRepository;
    private final OutBoundItemRepository outBoundItemRepository;

    @Transactional
    public void createOrder(StoreOrderDto orderDto) {
        Store store = storeDao.findById(orderDto.getStoreId().intValue())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다"));

        String orderNumber = generateOrderNumber(orderDto.getStoreId());

        StoreOrder savedOrder = storeOrderDao.save(
                StoreOrder.builder()
                        .orderNumber(orderNumber)
                        .storeId(orderDto.getStoreId())
                        .orderDate(LocalDateTime.now())
                        .build()
        );


        for (StoreOrderDetailDto item : orderDto.getItems()) {
            StoreOrderDetailId id = new StoreOrderDetailId(savedOrder.getId(), item.getItemCode());
            StoreOrderDetail detail = StoreOrderDetail.builder()
                    .orderId(id.getOrderId())
                    .itemCode(id.getItemCode())
                    .orderedQuantity(item.getOrderedQuantity())
                    .status("주문완료")
                    .storeOrder(savedOrder)
                    .build();
            storeOrderDetailDao.save(detail);

            OutBound outBound = OutBound.builder()
                    .storeId(orderDto.getStoreId())
                    .requiredDate(new Date())
                    .dueDate(null)
                    .status("출고요청")
                    .build();
            outBoundRepository.save(outBound);

            OutBoundItem outboundItem = OutBoundItem.builder()
                    .outBound(outBound)
                    .itemCode(item.getItemCode())
                    .receivedQuantity(item.getOrderedQuantity())
                    .build();
            outBoundItemRepository.save(outboundItem);
        }
    }

    public String generateOrderNumber(Integer storeId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int countToday = storeOrderRepository.countByStoreIdAndDate(storeId, today);
        return String.format("ORD-%d-%s-%03d", storeId, today, countToday + 1);
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