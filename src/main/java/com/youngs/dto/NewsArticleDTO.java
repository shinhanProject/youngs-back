package com.youngs.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticleDTO {
    private Long newsSeq; //세부 보도자료 인덱스
    private String title; //보도자료 제목
    private String url; //보도자료 URL
    private String description; //보도자료 요약
    private String pubDate; //보도자료 게시일
    private boolean wasWritten; // 요약 작성 여부

    public NewsArticleDTO(Long newsSeq, String title, String url, String pubDate, boolean wasWritten){
        this.newsSeq = newsSeq;
        this.title = title;
        this.url = url;
        this.pubDate = pubDate;
        this.wasWritten = wasWritten;
    }

    public NewsArticleDTO(Long newsSeq, String title, String url, String description, String pubDate){
        this.newsSeq = newsSeq;
        this.title = title;
        this.url = url;
        this.description = description;
        this.pubDate = pubDate;
    }
}
