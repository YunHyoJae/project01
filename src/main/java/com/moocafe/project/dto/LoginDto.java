package com.moocafe.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDto {
    private String userId;
    private String userName;
    private String storeId;
    private String storeName;
}
