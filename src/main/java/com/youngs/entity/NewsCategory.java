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
public class NewsCategory {
    @Id
    @Column(name = "category_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorySeq; //보도자료 카테고리 인덱스

    @Column(name="category_name", nullable = false)
    private String categoryName; //보도자료 카테고리 명

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt; //생성일
}
