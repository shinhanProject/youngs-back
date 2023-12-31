package com.youngs.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="news_article")
@EntityListeners(AuditingEntityListener.class)
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
    private String description; //보도자료 요약

    @Column(name="pub_date",nullable = false)
    private LocalDateTime pubDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_seq")
    private NewsCategory newsCategory; //보도자료 카테고리 인덱스

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt; //생성일
}
