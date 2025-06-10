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
        System.out.println("등록 요청: " + item.getItemCode() + ", " + item.getExpirationDate()); // 🔍 여기

        String sql = "INSERT INTO inventoryRegistration " +
                "(itemCode, itemName, itemGroup, itemStandard, itemQuantity, itemPrice, itemClass, expirationDate, itemDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getExpirationDate());

            return jdbcTemplate.update(sql,
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
        } catch (Exception e) {
            System.out.println("❌ INSERT 실패: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
