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
    private String returnNumber;
    private String returnNote;
    private List<ReturnItemDto> returnItems;
}