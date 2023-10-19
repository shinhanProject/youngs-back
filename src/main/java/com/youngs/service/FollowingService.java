package com.youngs.service;

import org.springframework.http.ResponseEntity;

public interface FollowingService {
    /**
     * 팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserSeq : 로그인한 사용자의 고유 번호
     * @param targetUserSeq : 팔로우할 사용자의 고유 번호
     * @return - 200 : 팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 팔로우 실패
     **/
    ResponseEntity<?> saveFollowing(Long currentUserSeq, Long targetUserSeq);

    /**
     * 언팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserSeq : 로그인한 사용자 고유 번호
     * @param targetUserSeq : 언팔로우할 사용자의 고유 번호
     * @return - 200 : 언팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 언팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 언팔로우 실패
     */
    ResponseEntity<?> deleteFollowing(Long currentUserSeq, Long targetUserSeq);
}
