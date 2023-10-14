package com.youngs.service;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.UserProfileDTO;
import com.youngs.dto.UserSandDTO;

import java.util.List;

public interface MyPageService {
    /**
     * 사용자 프로필 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    UserProfileDTO searchUserByUserSeq(Long userSeq);

    /**
     *  모래사장 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<UserSandDTO> searchUserSandList(Long userSeq);

    /**
     * 팔로우 목록 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<FollowingDTO> sarchFollowingList(Long userSeq);

    /**
     * 팔로워 목록 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<FollowingDTO> sarchFollowerList(Long userSeq);
}
