package com.moocafe.project.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class SaleRegisterDto {
    private Integer storeId;
    private String menuId;
    private String menuName;
    private Integer quantity;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime saleTime;
}