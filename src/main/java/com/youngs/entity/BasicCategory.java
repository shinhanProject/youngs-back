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
@Table(name ="basic_category")
@EntityListeners(AuditingEntityListener.class)
public class BasicCategory {
    @Id
    @Column(name = "category_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorySeq; //기초정보 카테고리 인덱스

    @Column(name="category_name", nullable = false)
    private String categoryName; //기초정보 카테고리 명

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt; //생성일
}
