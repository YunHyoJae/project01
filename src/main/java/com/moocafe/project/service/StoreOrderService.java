package com.moocafe.project.service;

import com.moocafe.project.dao.StoreDao;
import com.moocafe.project.dao.StoreOrderDao;
import com.moocafe.project.dao.StoreOrderDetailDao;
import com.moocafe.project.dto.ItemSearchDto;
import com.moocafe.project.dto.StoreOrderDetailDto;
import com.moocafe.project.dto.StoreOrderDto;
import com.moocafe.project.dto.StoreOrderListResponseDto;
import com.moocafe.project.entity.*;
import com.moocafe.project.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final InventoryStoreRepository inventoryStoreRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Page<ItemSearchDto> getItemSearchList(Pageable pageable) {
        String jpql = """
            SELECT new com.moocafe.project.dto.ItemSearchDto(
                i.itemCode,
                i.itemName,
                i.itemPrice,
                COALESCE(s.count, 0)
            )
            FROM InventoryItem i
            LEFT JOIN InventoryStore s ON i.itemCode = s.itemCode AND s.storeId = 1
            ORDER BY i.itemCode ASC
        """;

        TypedQuery<ItemSearchDto> query = entityManager.createQuery(jpql, ItemSearchDto.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<ItemSearchDto> results = query.getResultList();

        String countJpql = "SELECT COUNT(i) FROM InventoryItem i";
        Long total = entityManager.createQuery(countJpql, Long.class).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

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

            int stock = inventoryStoreRepository.findQuantityByStoreIdAndItemCode(1, item.getItemCode());
            if (item.getOrderedQuantity() > stock) {
                throw new IllegalArgumentException("[" + item.getItemCode() + "] 재고 부족: 주문수량이 재고보다 많습니다.");
            }

            StoreOrderDetailId id = new StoreOrderDetailId(savedOrder.getId(), item.getItemCode());
            StoreOrderDetail detail = StoreOrderDetail.builder()
                    .orderId(id.getOrderId())
                    .itemCode(id.getItemCode())
                    .orderedQuantity(item.getOrderedQuantity())
                    .status("출고요청")
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
                (String) obj[0],                     // orderNumber
                (String) obj[1],                     // orderDate
                (String) obj[2],                     // itemCode
                (String) obj[3],                     // itemName
                ((Number) obj[4]).intValue(),        // orderedQuantity
                (String) obj[5],                     // orderStatus
                obj[6] != null ? (String) obj[6] : null
        )).toList();
    }

    public List<Object[]> findRecentOrderListByStoreId(Integer storeId) {
        return storeOrderRepository.findRecentOrderListByStoreId(storeId);
    }

    public StoreOrder findByOrderNumber(String orderNumber) {
        return storeOrderDao.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주문번호: " + orderNumber));
    }
}
