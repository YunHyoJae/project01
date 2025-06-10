package com.moocafe.project.service;

import com.moocafe.project.dao.ItemSearchDao;
import com.moocafe.project.entity.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemSearchService {
    private final ItemSearchDao itemSearchDao;

    public List<InventoryItem> searchItems(String keyword) {
        return itemSearchDao.searchItems(keyword);
    }

    public List<InventoryItem> searchCode(String code) {
        return itemSearchDao.searchCode(code);
    }

    public List<InventoryItem> getAllItems() {
        return itemSearchDao.getAllItems();
    }
}




