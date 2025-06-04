package com.moocafe.project.dao;

import com.moocafe.project.entity.MenuPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MenuPriceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertMenuPrice(MenuPrice menuPrice) {
        String sql = "INSERT INTO MenuPrice (Id, menuId, MenuPrice) " +
                "VALUES (menu_price_seq.NEXTVAL, ?, ?)";
        return jdbcTemplate.update(sql,
                menuPrice.getMenuId(),
                menuPrice.getMenuPrice());
    }
}
