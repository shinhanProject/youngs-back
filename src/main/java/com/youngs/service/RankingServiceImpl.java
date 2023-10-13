package com.youngs.service;

import com.youngs.dto.UserRankDTO;
import com.youngs.entity.Following;
import com.youngs.entity.User;
import com.youngs.repository.FollowingRepository;
import com.youngs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RankingServiceImpl implements RankingService {
    private final UserRepository userRep;
    private final FollowingRepository followingRep;

    /***
     * 상위 30위 유저 정보 조회
     * @author 이지은
     * @return 상위 30위 유저 정보 리스트
     */
    @Override
    public List<UserRankDTO> getTop30UsersByPoint(Long userSeq) {
        // 포인트 기준으로 내림차순으로 조회
        List<User> userList = userRep.findAllOrderByPointDesc();
        if(userList.isEmpty()) { //가져온 유저정보가 없다면
            throw new RuntimeException("랭킹 정보를 확인할 수 없습니다");
        }
        List<UserRankDTO> userRankList = new ArrayList<>();
        int cnt = 0;
        for(User user: userList){
            int status = 0; //default: 자기 자신일 때
            cnt++;
            if(userSeq != null) { //로그인을 한 유저일 때
                if (!userSeq.equals(user.getUserSeq())) { //클릭한 타겟이 본인이 아닐 때
                    Following following = followingRep.findByFollowerAndAndFollowing(userSeq, user.getUserSeq());
                    status = (following != null ? 2 : 1); //팔로잉 중이라면 2, 아니라면 1
                }
            } else { //로그인을 하지 않은 유저일 때
                status = 1;
            }
            userRankList.add(new UserRankDTO(user.getUserSeq(), user.getNickname(), user.getProfile(), user.getPoint(), user.getTier(), status));
            if (cnt == 30) break; //상위 30개만
        }
        return userRankList;
    }
}
