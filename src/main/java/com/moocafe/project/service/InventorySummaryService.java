package com.moocafe.project.service;

import com.moocafe.project.dao.InventoryStoreDao;
import com.moocafe.project.dto.InventorySummaryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventorySummaryService {

    private final InventoryStoreDao inventoryStoreDao;

    public InventorySummaryService(InventoryStoreDao inventoryStoreDao) {
        this.inventoryStoreDao = inventoryStoreDao;
    }

    public List<InventorySummaryDto> getInventorySummary() {
        return inventoryStoreDao.getStoreInventorySummary();
    }
}
