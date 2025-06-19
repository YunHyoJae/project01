package com.moocafe.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requiredDate;

    private String returnNote;
    private String typeReturn;
    private String storeId;
    private String orderNumber;

    @Builder.Default
    private List<ReturnItemDto> items = new ArrayList<>();
}