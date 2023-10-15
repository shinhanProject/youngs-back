package com.youngs.service;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.UserProfileDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.entity.Following;
import com.youngs.entity.User;
import com.youngs.entity.UserSand;
import com.youngs.exception.NoChangeException;
import com.youngs.repository.FollowingRepository;
import com.youngs.repository.UserRepository;
import com.youngs.repository.UserSandRepository;
import com.youngs.security.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements  MyPageService{
    private final UserSandRepository userSandRep;
    private final FollowingRepository followingRep;
    private final UserRepository userRep;

    /**
     * 사용자 프로필 조회
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 조회할 사용자 고유 번호
     * @return 사용자 프로필 정보
     * @throws RuntimeException : 조회할 사용자 정보가 존재하지 않을 때
     */
    @Override
    public UserProfileDTO searchUserByUserSeq(PrincipalUserDetails currentUserDetails, Long userSeq) throws RuntimeException {
        User user = userRep.findByUserSeq(userSeq); // 조회할 사용자 정보
        if (user == null){ // 인덱스에 해당하는 유저가 없다면
            throw new RuntimeException("조회할 유저 정보가 없습니다.");
        }

        UserProfileDTO userProfile; // 조회한 사용자 프로필 정보
        int count = userSandRep.getByCountAndUserUserSeq(userSeq); // 누적 조개 수
        int status = 0; // 팔로우 유무 (본인일 경우 0)
        if (currentUserDetails != null) { // 로그인한 사용자가 있을 경우
            Long currentUserSeq = currentUserDetails.getUserSeq();
            if (currentUserSeq != userSeq) { // 로그인한 사용자가 조회하려는 프로필이 본인 프로필이 아닐 경우
                Following following = followingRep.findByFollowerAndAndFollowing(userSeq, user.getUserSeq());
                status = (following != null ? 2 : 1); // 팔로잉 중이라면 2, 아니라면 1
            }

            userProfile = new UserProfileDTO(user.getNickname(), user.getProfile(), user.getTier(), count, status);
        }
        else { // 로그인한 사용자가 없을 경우
            status = 1;
            userProfile = new UserProfileDTO(user.getNickname(), user.getProfile(), user.getTier(), count, status);
        }

        return userProfile;
    }

    /**
     * 사용자 프로필 변경 - 프로필 이미지 및 닉네임
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @param profile 사용자 프로필
     * @param nickname 사용자 닉네임
     * @exception RuntimeException 사용자가 존재 하지 않을 떼
     * @exception RuntimeException 변경된 사항이 없을 떼
     * */
    @Override
    public void changeProfile(Long userSeq, String profile, String nickname) throws RuntimeException {
        User user = userRep.findByUserSeq(userSeq);
        if(user == null){
            throw new RuntimeException("사용자가 존재 하지 않습니다.");
        }

        //프로필 이미지와 닉네임에 변화가 없다면
        if(user.getProfile().equals(profile) && user.getNickname().equals(nickname)){
            throw new NoChangeException("프로필 변경 사항이 없습니다.");
        }

        user.setProfile(profile);
        user.setNickname(nickname);
        userRep.save(user); //변경한 프로필 및 닉네임 저장
    }

    /**
     * 모래사장 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @exception RuntimeException 가져온 모래사장 정보가 없다면 throw
     * @return 모래사장 데이터
     * */
    @Override
    public List<UserSandDTO> searchUserSandList(Long userSeq) {
        List<UserSand> userSandList = userSandRep.findAllByUserUserSeq(userSeq);
        if(userSandList.isEmpty()){ //가져온 모래사장이 비어있다면
            throw new RuntimeException("조회할 모래사장 정보가 없습니다.");
        }

        List<UserSandDTO> userSandDTOList = new ArrayList<>();
        for(UserSand clam: userSandList){
            String createdAt = clam.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            userSandDTOList.add(new UserSandDTO(clam.getCount(), createdAt));
        }
        return userSandDTOList;
    }

    /**
     * 팔로잉 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @exception RuntimeException 가져온 팔로잉 목록 정보가 없다면 throw
     * @return 팔로잉 목록
     * */
    @Override
    public List<FollowingDTO> sarchFollowingList(Long userSeq){
        List<Following> followingList = followingRep.findAllByFollowerUserSeq(userSeq);
        if(followingList.isEmpty()){
            throw new RuntimeException("조회할 팔로잉 목록이 없습니다.");
        }
        List<FollowingDTO> followingDTOList = new ArrayList<>();
        for(Following f : followingList){
            //팔로우 리스트로 불러온 사용자의 정보 조회
            User user = userRep.findByUserSeq(f.getFollowing().getUserSeq());

            int status = 0; //default: 자기 자신일 때
            if(userSeq != null) { //로그인을 한 유저일 때
                if (!userSeq.equals(user.getUserSeq())) { //클릭한 타겟이 본인이 아닐 때
                    Following following = followingRep.findByFollowerAndAndFollowing(userSeq, user.getUserSeq());
                    status = (following != null ? 2 : 1); //팔로잉 중이라면 2, 아니라면 1
                }
            } else { //로그인을 하지 않은 유저일 때
                status = 1;
            }
            followingDTOList.add(new FollowingDTO(user.getNickname(), user.getProfile(), status));
        }
        return followingDTOList;
    }

    /**
     * 팔로워 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @exception RuntimeException 가져온 팔로워 목록 정보가 없다면 throw
     * @return 팔로워 목록
     * */
    @Override
    public List<FollowingDTO> sarchFollowerList(Long userSeq){
        List<Following> followerList = followingRep.findAllByFollowingUserSeq(userSeq);
        if(followerList.isEmpty()){
            throw new RuntimeException("조회할 팔로워 목록이 없습니다.");
        }
        List<FollowingDTO> followerDTOList = new ArrayList<>();
        for(Following f : followerList){
            //팔로우 리스트로 불러온 사용자의 정보 조회
            User user = userRep.findByUserSeq(f.getFollower().getUserSeq());

            int status = 0; //default: 자기 자신일 때
            if(userSeq != null) { //로그인을 한 유저일 때
                if (!userSeq.equals(user.getUserSeq())) { //클릭한 타겟이 본인이 아닐 때
                    Following following = followingRep.findByFollowerAndAndFollowing(userSeq, user.getUserSeq());
                    status = (following != null ? 2 : 1); //팔로잉 중이라면 2, 아니라면 1
                }
            } else { //로그인을 하지 않은 유저일 때
                status = 1;
            }
            followerDTOList.add(new FollowingDTO(user.getNickname(), user.getProfile(), status));
        }
        return followerDTOList;
    }
}
