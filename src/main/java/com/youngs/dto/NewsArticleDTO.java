package com.youngs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticleDTO {
    private Long newsSeq; //세부 보도자료 인덱스
    private String title; //보도자료 제목
    private String url; //보도자료 URL
}
