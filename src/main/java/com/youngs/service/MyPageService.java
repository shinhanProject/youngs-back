package com.youngs.service;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.UserProfileDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.security.PrincipalUserDetails;
import org.springframework.http.ResponseEntity;

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
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 편집할 프로필의 사용자의 고유 번호
     * @param profile : 변경할 프로필 이미지 이름
     * @param nickname : 변경할 닉네임
     * @return - 프로필 편집 성공 시 : 200
     * @return - 프로필 변경 사항이 없을 경우 : 204
     * @return - 로그인하지 않은 상태로 프로필 편집을 시도했을 경우 : 401
     * @return - 프로필 편집 실패 시 : 500
     **/
    ResponseEntity<?> changeProfile(PrincipalUserDetails currentUserDetails, Long userSeq, String profile, String nickname);

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
