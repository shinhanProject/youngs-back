package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSandDTO {
    private int count; // 조개 누적 개수
    private String createdAt; // 생성일
}