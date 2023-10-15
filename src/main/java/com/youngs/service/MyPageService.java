package com.youngs.service;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.UserProfileDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.security.PrincipalUserDetails;

import java.util.List;

public interface MyPageService {
    /**
     * 사용자 프로필 조회
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 조회할 사용자 고유 번호
     * @return : 사용자 프로필 정보
     **/
    UserProfileDTO searchUserByUserSeq(PrincipalUserDetails currentUserDetails, Long userSeq);

    /**
     * 사용자 프로필 변경 - 프로필 이미지 및 닉네임
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param profile 사용자 프로필
     * @param nickname 사용자 닉네임
     * */
    void changeProfile(Long userSeq, String profile, String nickname);

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
