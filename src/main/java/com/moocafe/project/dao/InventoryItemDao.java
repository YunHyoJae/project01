package com.moocafe.project.dao;

import com.moocafe.project.dto.InventoryItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Repository
public class InventoryItemDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertItem(InventoryItemDto item) {
        System.out.println("등록 요청: " + item.getItemCode() + ", " + item.getExpirationDate());

        String sql = "INSERT INTO inventoryRegistration " +
                "(itemCode, itemName, itemGroup, itemStandard, itemQuantity, itemPrice, itemClass, expirationDate, itemDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String storeSql = "INSERT INTO inventoryStore (itemCode, storeId, count, regDate) VALUES (?, ?, ?, ?)";

        try {
            java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getExpirationDate());

            int result1 = jdbcTemplate.update(sql,
                    item.getItemCode(),
                    item.getItemName(),
                    item.getItemGroup(),
                    item.getItemStandard(),
                    item.getItemQuantity(),
                    item.getItemPrice(),
                    item.getItemClass(),
                    new java.sql.Date(parsedDate.getTime()),
                    Timestamp.valueOf(LocalDateTime.now())
            );

            // 무조건 본사 재고용으로 사용
            int storeId = 1;
            int result2 = jdbcTemplate.update(storeSql,
                    item.getItemCode(),
                    storeId,
                    0, // count=0
                    new java.sql.Date(System.currentTimeMillis())
            );
            System.out.println("inventoryStore insert result: " + result2);

            return result1; // or result1 * result2 > 0 ? 1 : 0

        } catch (Exception e) {
            System.out.println("❌ INSERT 실패: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
