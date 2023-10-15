package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicArticleDTO {
    private Long basicSeq; //세부 기초지식 인덱스
    private String subject; //용어 또는 제목
    private String information; // 기초지식 설명 또는 파일명 or 기초지식  내용 요약
}

