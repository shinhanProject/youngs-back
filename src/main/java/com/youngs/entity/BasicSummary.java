package com.youngs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="basic_summary")
@EntityListeners(AuditingEntityListener.class)
public class BasicSummary {
    @Id
    @Column(name = "summary_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long summarySeq; // 기초 지식 요약 고유 번호

    @Column(nullable = false)
    private String context; // 한 줄 요약 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_seq")
    private User user; // 작성한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "basic_seq")
    private BasicArticle basicArticle; // 요약한 기초 지식

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; // 수정일

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성일
}
