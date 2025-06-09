package com.moocafe.project.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnDto {
    private int id;
    private String returnNumber;
    private String orderNumber;
    private LocalDateTime requiredDate;
    private String returnNote;
    private String typeReturn;
    private List<ReturnItemDto> items;
}
