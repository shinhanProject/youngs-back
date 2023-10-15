package com.youngs.repository;

import com.youngs.entity.Following;
import com.youngs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, Integer> {
    /**
     * 팔로우 대상(타겟)을 팔로우 중인 지 조회
     * @author 이지은
     * @param userSeq 로그인한 사용자의 인덱스
     * @param targetUserSeq 팔로우 대상의 인덱스
     * */
    @Query(value = "select f from Following f where f.follower.userSeq =?1 and f.following.userSeq=?2")
    Following findByFollowerAndAndFollowing(Long userSeq, Long targetUserSeq);

    List<Following> findAllByFollowerUserSeq(Long userSeq);

    List<Following> findAllByFollowingUserSeq(Long userSeq);

    /**
     * 언팔로우 하기
     * @author 이지은
     * @param user 로그인한 사용자
     * @param targetUser 언팔로우 대상자
     * */
    void deleteByFollowerAndFollowing(User user, User targetUser);
}
