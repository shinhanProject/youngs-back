package com.youngs.entity;

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
@Table(name ="user_sand")
@EntityListeners(AuditingEntityListener.class)
public class UserSand {
    @Id
    @Column(name ="usersand_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSandSeq; //모래사장 인덱스

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(nullable = false)
    private int count; //요약 갯수

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; //수정일

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; //생성일
}
