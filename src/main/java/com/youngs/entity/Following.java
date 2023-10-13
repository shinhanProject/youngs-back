package com.youngs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "following")
@IdClass(FollowingId.class) // 복합 기본 키 클래스를 지정
@EntityListeners(AuditingEntityListener.class)
public class Following implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "follower_seq")
    private User follower; // 팔로우하는 유저

    @Id
    @ManyToOne
    @JoinColumn(name = "following_seq")
    private User following; // 팔로우 대상 유저

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 팔로우 시작 날짜
}

class FollowingId implements Serializable {
    private Long follower;
    private Long following;
}
