package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRankDTO {
    private Long userSeq; // 사용자 고유 번호
    private String nickname; // 사용자 닉네임
    private String profile; // 사용자 프로필 이미지 이름
    private int point; // 사용자 점수
    private String tier; // 사용자 티어
    private int status; //팔로우 유무
}
