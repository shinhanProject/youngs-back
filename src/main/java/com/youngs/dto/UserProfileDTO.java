package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String nickname; // 유저 닉네임
    private String profile; // 유저 프로필
    private String tier; // 유저 티어
    private int count; // 누적 조개 수
    private int isPrivate; //요약 정보 공개 여부
    private int status; // 팔로우 유무
}
