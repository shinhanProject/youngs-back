package com.youngs.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticle {
    @Id
    @Column(name = "news_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsSeq; //세부 보도자료 인덱스

    @Column(nullable = false)
    private String title; //세부 보도자료 재목

    @Column(nullable = false)
    private String url; //세부 보도자료 URL

    @Column(nullable = false)
    private Long categorySeq; //보도자료 카테고리 인덱스

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt; //생성일
}
