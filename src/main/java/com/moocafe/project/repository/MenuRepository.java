package com.moocafe.project.repository;


import com.moocafe.project.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByMenuId(String menuId);
}
