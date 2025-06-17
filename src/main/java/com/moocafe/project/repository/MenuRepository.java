package com.moocafe.project.repository;


import com.moocafe.project.dto.MenuSimpleDto;
import com.moocafe.project.dto.SimpleMenuDto;
import com.moocafe.project.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByMenuId(String menuId);
    @Query("SELECT DISTINCT new com.moocafe.project.dto.SimpleMenuDto(m.menuId, m.menuName) " +
            "FROM Menu m " +
            "JOIN MenuPrice p ON m.menuId = p.menuId")
    List<SimpleMenuDto> findMenusWithPrice();

    @Query("""
    SELECT m FROM Menu m
    WHERE m.menuId = :menuId
""")
    List<Menu> findByMenuIdWithPrice(@Param("menuId") String menuId);

    @Query("SELECT m FROM Menu m WHERE m.menuId = :menuId")
    List<Menu> findAllByMenuId(@Param("menuId") String menuId);

    @Query("""
    SELECT DISTINCT m
    FROM Menu m
    WHERE m.menuId IN (
        SELECT p.menuId FROM MenuPrice p
    )
""")
    List<Menu> findMenusWithPriceOnly();

    @Query("""
    SELECT DISTINCT new com.moocafe.project.dto.MenuSimpleDto(
        m.menuId, m.menuName, mp.menuPrice
    )
    FROM Menu m
    JOIN MenuPrice mp ON m.menuId = mp.menuId
""")
    List<MenuSimpleDto> findDistinctMenusWithPrice();

    Optional<Menu> findFirstByMenuId(String menuId);

    boolean existsByMenuId(String menuId);

    @Query("""
    SELECT m FROM Menu m
    WHERE m.itemCode = :itemCode
""")
    List<Menu> findByItemCode(@Param("itemCode") String itemCode);

    @Query("""
SELECT new com.moocafe.project.dto.MenuSimpleDto(
    m.menuId, m.menuName, mp.menuPrice
)
FROM Menu m
JOIN MenuPrice mp ON m.menuId = mp.menuId
WHERE mp.menuPrice IS NOT NULL
GROUP BY m.menuId, m.menuName, mp.menuPrice
""")
    List<MenuSimpleDto> findDistinctMenuSimpleDtos();

}
