package com.moocafe.project.service;

import com.moocafe.project.dao.MenuDao;
import com.moocafe.project.dao.MenuPriceDao;
import com.moocafe.project.dto.MenuDto;
import com.moocafe.project.dto.MenuRegisterDto;
import com.moocafe.project.dto.MenuSimpleDto;
import com.moocafe.project.entity.Menu;
import com.moocafe.project.entity.MenuPrice;
import com.moocafe.project.repository.MenuPriceRepository;
import com.moocafe.project.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuDao menuDao;
    private final MenuPriceDao menuPriceDao;
    private final MenuRepository menuRepository;
    private final MenuPriceRepository menuPriceRepository;

    @Transactional
    public void registerMenuWithItems(MenuRegisterDto dto) {
        for (MenuDto item : dto.getMenuItems()) {
            Menu menu = new Menu(dto.getMenuId(), dto.getMenuName(), item.getItemCode(), item.getQuantityUsed());
            menuDao.insertMenu(menu);
        }
        MenuPrice price = new MenuPrice(dto.getMenuId(), dto.getMenuPrice());
        menuPriceDao.insertMenuPrice(price);
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public List<Menu> findMenusWithPriceOnly() {
        return menuRepository.findMenusWithPriceOnly();
    }

    public List<MenuSimpleDto> getMenuListForStoreOwner() {
        return menuRepository.findDistinctMenusWithPrice();
    }

    public String getMenuNameById(String menuId) {
        return menuRepository.findFirstByMenuId(menuId)
                .map(Menu::getMenuName)
                .orElse("메뉴명 없음");
    }

    // 컨트롤러에서 menuRepository.findByMenuIdWithPrice() 대신 호출
    public List<Menu> findByMenuIdWithPrice(String menuId) {
        return menuRepository.findByMenuIdWithPrice(menuId);
    }

    public List<Menu> findByItemCode(String itemCode) {
        return menuRepository.findByItemCode(itemCode);
    }

    public List<Menu> findMenusWithPrice() {
        return menuRepository.findMenusWithPriceOnly();
    }

    public Optional<MenuPrice> findByMenuId(String menuId) {
        return menuPriceRepository.findByMenuId(menuId);
    }

    public List<MenuSimpleDto> getDistinctMenuListForSaleInput() {
        return menuRepository.findDistinctMenuSimpleDtos();
    }

    public boolean existsByMenuId(String menuId) {
        return menuRepository.existsByMenuId(menuId);
    }
}