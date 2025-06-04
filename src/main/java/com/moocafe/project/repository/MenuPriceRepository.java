package com.moocafe.project.repository;

import com.moocafe.project.entity.MenuPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuPriceRepository extends JpaRepository<MenuPrice, Long> {
    Optional<MenuPrice> findByMenuId(String menuId);
}
