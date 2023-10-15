package com.youngs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="basic_article")
@EntityListeners(AuditingEntityListener.class)
public class BasicArticle {
    @Id
    @Column(name = "basic_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basicSeq; //기초지식 인덱스

    @Column(nullable = false)
    private String subject; //용어 또는 제목

    @Column(nullable = false)
    private String context; //기초지식 설명 또는 파일명

    @Column(nullable = false)
    private String description; //기초지식 내용 요약

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_seq")
    private BasicCategory basicCategory; //기초지식 카테고리 인덱스

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt; //생성일
}
