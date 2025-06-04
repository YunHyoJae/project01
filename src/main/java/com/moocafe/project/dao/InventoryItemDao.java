package com.moocafe.project.dao;

import com.moocafe.project.dto.InventoryItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryItemDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertItem(InventoryItemDto item) {
        String sql = "INSERT INTO inventoryRegistration " +
                "(itemCode, itemName, itemGroup, itemStandard, itemQuantity, itemPrice, itemClass, expirationDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                item.getItemCode(),
                item.getItemName(),
                item.getItemGroup(),
                item.getItemStandard(),
                item.getItemQuantity(),
                item.getItemPrice(),
                item.getItemClass(),
                item.getExpirationDate());
    }
}
