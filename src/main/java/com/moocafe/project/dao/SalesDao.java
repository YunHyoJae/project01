package com.moocafe.project.dao;

import com.moocafe.project.entity.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SalesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertSale(Sales sales) {
        String sql = "INSERT INTO Sales (SaleId, StoreId, MenuId, MenuName, Quantity) " +
                "VALUES (sales_seq.NEXTVAL, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                sales.getStoreId(),
                sales.getMenuId(),
                sales.getMenuName(),
                sales.getQuantity());
    }
}
