package com.moocafe.project.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private Page<T> page;
    private String category;
    private String keyword;
    private String url;
}
