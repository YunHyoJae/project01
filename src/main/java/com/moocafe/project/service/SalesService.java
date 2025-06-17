package com.moocafe.project.service;

import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.Sales;
import com.moocafe.project.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;

@Service
public class SalesService {

    private final SalesRepository salesRepository;

    @Autowired
    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SalesSummaryDto> getSalesSummary(Integer storeId, LocalDate startDate, LocalDate endDate) {
        return salesRepository.findSummaryByStoreAndDate(storeId, startDate, endDate);
    }

    public void saveSaleEntity(Sales sale) {
        salesRepository.save(sale);
    }


    public List<SalesSummaryDto> getSalesSummary(Integer storeId, LocalDate startDate, LocalDate endDate, String menuName) {
        List<SalesSummaryDto> rawList = getSalesSummary(storeId, startDate, endDate);
        if (menuName != null && !menuName.trim().isEmpty()) {
            return rawList.stream()
                    .filter(dto -> dto.getMenuName() != null && dto.getMenuName().contains(menuName))
                    .toList();
        }
        return rawList;
    }

    public int sumQuantityByStoreIdAndMenuIdAndPeriod(Integer storeId, String menuId, Date startDate, Date endDate) {
        return salesRepository.sumQuantityByStoreIdAndMenuIdAndPeriod(storeId, menuId, startDate, endDate);
    }

    public List<Sales> findByStoreIdAndSaleTimeBetween(Integer storeId, Date startDate, Date endDate) {
        return salesRepository.findByStoreIdAndSaleTimeBetween(storeId, startDate, endDate);
    }
    public List<Sales> findAll() {
        return salesRepository.findAll();
    }
    public List<SalesSummaryDto> findSalesSummary(Integer storeId, Date startDate, Date endDate) {
        return salesRepository.findSalesSummaryByDate(storeId, startDate, endDate);
    }
    public void save(Sales sale) {
        salesRepository.save(sale);
    }

    public List<SalesSummaryDto> findSalesSummaryTotal(Integer storeId, Date startDate, Date endDate) {
        return salesRepository.findSalesSummaryTotal(storeId, startDate, endDate);
    }

    public List<SalesSummaryDto> getSalesMenuSummary(Integer storeId, LocalDate startDate, LocalDate endDate, String menuName) {
        String menuNameParam = (menuName == null || menuName.isBlank()) ? null : "%" + menuName + "%";
        return salesRepository.findMenuSummary(storeId, startDate, endDate, menuNameParam);
    }

}
