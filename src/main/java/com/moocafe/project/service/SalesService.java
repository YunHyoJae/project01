package com.moocafe.project.service;

import com.moocafe.project.dto.SalesSummaryDto;
import com.moocafe.project.entity.Sales;
import com.moocafe.project.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
}
