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
@Table(name ="word")
@EntityListeners(AuditingEntityListener.class)
public class Word {
    @Id
    @Column(name = "word_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordSeq; // 단어 인덱스

    @Column(nullable = false)
    private String name; //단어 명

    @Column(nullable = false)
    private String description; //단어 설명

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt; //생성일
}
