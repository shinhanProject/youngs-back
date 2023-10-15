package com.youngs.service;

import com.youngs.entity.Following;
import com.youngs.entity.User;
import com.youngs.repository.FollowingRepository;
import com.youngs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowingServiceImpl implements FollowingService{
    private final UserRepository userRep;
    private final FollowingRepository followingRep;

    /**
     * 팔로우 하기
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param targetUserSeq 팔로우 할 타겟의 인덱스
     * @exception RuntimeException 대상자를 찾을 수 없을 떄
     * */
    @Override
    public void saveFollowing(Long userSeq, Long targetUserSeq) throws RuntimeException {
        User user = userRep.findByUserSeq(userSeq);
        User targetUser = userRep.findByUserSeq(targetUserSeq);

        //사용자와 팔루우할 대상이 모두 null이 아닐 때
        if(user != null && targetUser != null){
            //팔로우가 되어 있는 지 체크
            Following following = followingRep.findByFollowerAndAndFollowing(userSeq, targetUserSeq);
            if(following != null) { //팔로우 중
                throw new RuntimeException("이미 팔로우한 대상자입니다.");
            }

            Following newFollowing = Following.builder()
                            .follower(user)
                            .following(targetUser)
                            .build();
            followingRep.save(newFollowing);
        } else {
            throw new RuntimeException("대상자를 찾을 수 없습니다.");
        }
    }

    /**
     * 언팔로우 하기
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param targetUserSeq 언팔로우 할 타겟의 인덱스
     * @exception RuntimeException 대상자를 찾을 수 없을 떄
     * */
    @Override
    public void deleteFollowing(Long userSeq, Long targetUserSeq){
        User user = userRep.findByUserSeq(userSeq);
        User targetUser = userRep.findByUserSeq(targetUserSeq);

        //사용자와 팔루우할 대상이 모두 null이 아닐 때
        if(user != null && targetUser != null){
            //팔로우가 되어 있는 지 체크
            Following following = followingRep.findByFollowerAndAndFollowing(userSeq, targetUserSeq);
            if(following == null) { //팔로우되어 있지 않다면
                throw new RuntimeException("언팔로우한 대상자가 아닙니다.");
            }
            followingRep.deleteByFollowerAndFollowing(user, targetUser);
        } else {
            throw new RuntimeException("대상자를 찾을 수 없습니다.");
        }
    }
}
