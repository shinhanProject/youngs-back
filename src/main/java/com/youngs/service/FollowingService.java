package com.youngs.service;

public interface FollowingService {
    /**
     * 팔로우 하기
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param targetUserSeq 팔로우 할 타겟의 인덱스
     * */
    void saveFollowing(Long userSeq, Long targetUserSeq);

    /**
     * 언팔로우 하기
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param targetUserSeq 언팔로우 할 타겟의 인덱스
     * */
    void deleteFollowing(Long userSeq, Long targetUserSeq);
}
