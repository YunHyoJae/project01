package com.moocafe.project.dao;


import com.moocafe.project.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertMenu(Menu menu) {
        String sql = "INSERT INTO Menu (Id, menuId, MenuName, ItemCode, QuantityUsed) " +
                "VALUES (menu_seq.NEXTVAL, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getItemCode(),
                menu.getQuantityUsed());
    }
}
