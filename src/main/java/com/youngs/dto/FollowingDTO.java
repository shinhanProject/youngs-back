package com.youngs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowingDTO {
    private String nickname; //유저 닉네임
    private String profile; //유저 프로필
    private int status; //팔로우 유무
}
