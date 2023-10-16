package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {
    Long summarySeq; // 요약 고유 번호
    String category; // 글 카테고리
    Long articleId; // 글 고유 번호
    String context; // 요약 내용
}
