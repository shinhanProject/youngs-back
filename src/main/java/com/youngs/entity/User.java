package com.youngs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 사용자 정보를 나타내는 엔티티 클래스
 * @author : 박상희
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PRIMARY KEY AUTO_INCREMENT
    private Long userSeq; // 사용자 고유 번호

    @Column(name = "email", nullable = false)
    private String email; // 사용자 이메일

    @Column(name = "nickname", nullable = false)
    private String nickname; // 사용자 닉네임

    @Column(name = "user_pw")
    private String userPw; // 사용자 비밀번호

    @Column(name = "profile", nullable = false)
    private String profile; // 사용자 프로필 이미지 이름

    @Column(name = "age")
    private int age; // 사용자 나이

    @Column(name = "point", nullable = false)
    private Integer point; // 사용자 점수

    @Column(name = "tier", nullable = false)
    private String tier; // 사용자 티어

    @Column(name = "is_private", nullable = false)
    private int isPrivate; // 사용자 요약 정보 공개 여부

    @Column(name = "refresh_token")
    private String refreshToken; // 사용자 Refresh Token

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 사용자 계정 생성 날짜

    @PrePersist // 엔티티 객체가 영구 저장소에 저장되기 전에 실행해야 하는 메서드를 지정하는 어노테이션
    public void prePersist(){
        this.profile = this.profile == null ? "SOL" : this.profile; // 사용자 프로필 이미지 이름 기본 설정
        this.point = this.point == null ? 0 : this.point; // 사용자 점수 기본 설정
        this.tier = this.tier == null ? "주식 왕초보" : this.tier; // 사용자 티어 기본 설정
    }
}
