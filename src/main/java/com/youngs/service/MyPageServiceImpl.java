package com.youngs.service;

import com.youngs.dto.*;
import com.youngs.entity.Following;
import com.youngs.entity.User;
import com.youngs.entity.UserSand;
import com.youngs.exception.NoChangeException;
import com.youngs.repository.FollowingRepository;
import com.youngs.repository.NewsSummaryRepository;
import com.youngs.repository.UserRepository;
import com.youngs.repository.UserSandRepository;
import com.youngs.security.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements  MyPageService {
    private final UserSandRepository userSandRep;
    private final FollowingRepository followingRep;
    private final UserRepository userRep;
    private final NewsSummaryRepository summaryRep;

    /**
     * 사용자 프로필 조회
     *
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq            : 조회할 사용자 고유 번호
     * @return 사용자 프로필 정보
     * @throws RuntimeException : 조회할 사용자 정보가 존재하지 않을 때
     * @author : 박상희, 이지은
     */
    @Override
    public UserProfileDTO searchUserByUserSeq(PrincipalUserDetails currentUserDetails, Long userSeq) throws RuntimeException {
        User user = userRep.findByUserSeq(userSeq); // 조회할 사용자 정보
        if (user == null) { // 인덱스에 해당하는 유저가 없다면
            throw new RuntimeException("조회할 유저 정보가 없습니다.");
        }

        UserProfileDTO userProfile; // 조회한 사용자 프로필 정보
        int count = userSandRep.getByCountAndUserUserSeq(userSeq); // 누적 조개 수
        int status = 0; // 팔로우 유무 (본인일 경우 0)
        if (currentUserDetails != null) { // 로그인한 사용자가 있을 경우
            Long currentUserSeq = currentUserDetails.getUserSeq();
            if (!currentUserSeq.equals(userSeq)) { // 로그인한 사용자가 조회하려는 프로필이 본인 프로필이 아닐 경우
                Following following = followingRep.findByFollowerAndAndFollowing(currentUserSeq, user.getUserSeq());
                status = (following != null ? 2 : 1); // 팔로잉 중이라면 2, 아니라면 1
            }
        } else { // 로그인한 사용자가 없을 경우
            status = 1;
        }
        userProfile = new UserProfileDTO(user.getNickname(), user.getProfile(), user.getTier(), count, status);
        return userProfile;
    }

    /**
     * 사용자 프로필 변경 - 프로필 이미지 및 닉네임
     *
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq            : 프로필 편집할 사용자의 고유 번호
     * @param profile            : 변경할 프로필 이미지 이름
     * @param nickname           : 변경할 닉네임
     * @return - 프로필 편집 성공 시 : 200
     * @return - 프로필 변경 사항이 없을 경우 : 204
     * @return - 로그인하지 않은 상태로 프로필 편집을 시도했을 경우 : 401
     * @return - 프로필 편집 실패 시 : 500
     * @throws RuntimeException  - 편집할 프로필의 사용자가 존재하지 않을 경우
     * @throws NoChangeException - 프로필 변경 사항이 없을 경우
     * @author : 박상희, 이지은
     **/
    @Override
    public ResponseEntity<?> changeProfile(PrincipalUserDetails currentUserDetails, Long userSeq, String profile, String nickname) throws RuntimeException {
        try {
            if (currentUserDetails != null) { // 로그인한 사용자가 있을 경우
                User user = userRep.findByUserSeq(userSeq);
                if (user == null) {
                    throw new RuntimeException("사용자가 존재 하지 않습니다.");
                }

                if (currentUserDetails.getUserSeq() == userSeq) { // 로그인한 사용자의 고유 번호와 편집할 프로필의 사용자의 고유 번호가 같을 경우
                    // 프로필 이미지와 닉네임에 변화가 없다면
                    if (user.getProfile().equals(profile) && user.getNickname().equals(nickname)) {
                        throw new NoChangeException("프로필 변경 사항이 없습니다.");
                    }

                    user.setProfile(profile);
                    user.setNickname(nickname);
                    userRep.save(user); // 변경한 프로필 및 닉네임 저장

                    return ResponseEntity.ok().body("프로필 변경에 성공했습니다.");
                } else { // 로그인한 사용자의 고유 번호와 편집할 프로필의 사용자의 고유 번호가 다를 경우
                    return ResponseEntity
                            .internalServerError() // 500
                            .body("프로필 변경이 불가능합니다.");
                }
            } else { // 로그인한 사용자가 없을 경우
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED) // 401 Error
                        .body("프로필 편집 전, 로그인해 주세요.");
            }
        } catch (NoChangeException e) { // 변경 사항이 없을 떄
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204
                    .body(e.getMessage());
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 모래사장 조회
     *
     * @param userSeq 사용자 인덱스
     * @return 모래사장 데이터
     * @throws RuntimeException 가져온 모래사장 정보가 없다면 throw
     * @author 이지은
     */
    @Override
    public List<UserSandDTO> searchUserSandList(Long userSeq) {
        List<UserSand> userSandList = userSandRep.findAllByUserUserSeq(userSeq);
        if (userSandList.isEmpty()) { //가져온 모래사장이 비어있다면
            throw new RuntimeException("조회할 모래사장 정보가 없습니다.");
        }

        List<UserSandDTO> userSandDTOList = new ArrayList<>();
        for (UserSand clam : userSandList) {
            String createdAt = clam.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            userSandDTOList.add(new UserSandDTO(clam.getCount(), createdAt));
        }
        return userSandDTOList;
    }

    /**
     * 팔로잉 조회
     *
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq 사용자 인덱스
     * @return 팔로잉 목록
     * @throws RuntimeException 가져온 팔로잉 목록 정보가 없다면 throw
     * @author 이지은
     */
    @Override
    public List<FollowingDTO> sarchFollowingList(PrincipalUserDetails currentUserDetails, Long userSeq) {
        List<Following> followingList = followingRep.findAllByFollowerUserSeq(userSeq);
        if (followingList.isEmpty()) {
            throw new RuntimeException("조회할 팔로잉 목록이 없습니다.");
        }

        List<FollowingDTO> followingDTOList = new ArrayList<>();
        for (Following f : followingList) {
            //팔로우 리스트로 불러온 사용자의 정보 조회
            User user = userRep.findByUserSeq(f.getFollowing().getUserSeq());
            int status = 0; //0: 자기 자신일 때
            if (currentUserDetails != null) { //로그인을 한 유저일 때
                Long currentUserSeq = currentUserDetails.getUserSeq();
                if (currentUserSeq.equals(userSeq)) { //로그인한 유저와 팔로잉 목록 조회 타겟이 같을 때
                    status = 2;
                } else if(!currentUserSeq.equals(user.getUserSeq())) {
                    Following following = followingRep.findByFollowerAndAndFollowing(currentUserSeq, user.getUserSeq());
                    status = (following != null ? 2 : 1); //팔로잉 중이라면 2, 아니라면 1
                }
            } else { //로그인을 하지 않은 유저일 때
                status = 1;
            }
            followingDTOList.add(new FollowingDTO(user.getUserSeq(), user.getNickname(), user.getProfile(), status));
        }
        return followingDTOList;
    }

    /**
     * 팔로워 조회
     *
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq 사용자 인덱스
     * @return 팔로워 목록
     * @throws RuntimeException 가져온 팔로워 목록 정보가 없다면 throw
     * @author 이지은
     */
    @Override
    public List<FollowingDTO> sarchFollowerList(PrincipalUserDetails currentUserDetails, Long userSeq) {
        List<Following> followerList = followingRep.findAllByFollowingUserSeq(userSeq);
        if (followerList.isEmpty()) {
            throw new RuntimeException("조회할 팔로워 목록이 없습니다.");
        }
        List<FollowingDTO> followerDTOList = new ArrayList<>();
        for (Following f : followerList) {
            //팔로우 리스트로 불러온 사용자의 정보 조회
            User user = userRep.findByUserSeq(f.getFollower().getUserSeq());
            int status = 0; //default: 자기 자신일 때
            if (currentUserDetails != null) { //로그인을 한 유저일 때
                Long currentUserSeq = currentUserDetails.getUserSeq();
                if (!currentUserSeq.equals(user.getUserSeq())) { //클릭한 타겟이 본인이 아닐 때
                    Following following = followingRep.findByFollowerAndAndFollowing(currentUserSeq, user.getUserSeq());
                    status = (following != null ? 2 : 1); //팔로잉 중이라면 2, 아니라면 1
                }
            } else { //로그인을 하지 않은 유저일 때
                status = 1;
            }
            followerDTOList.add(new FollowingDTO(user.getUserSeq(), user.getNickname(), user.getProfile(), status));
        }
        return followerDTOList;
    }

    /**
     * 사용자 요약 정보 최신 순 조회
     *
     * @author 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq            : 프로필 편집할 사용자의 고유 번호
     * @exception RuntimeException 500 요약 정보 조회에 실패
     * @exception RuntimeException 204 비공개 게시물
     * @exception RuntimeException 404 빈 게시물
     * @return 최신순으로 정렬한 요약 정보 리스트
     */
    public ResponseEntity<?> searchSummary(PrincipalUserDetails currentUserDetails, Long userSeq) throws RuntimeException {
        try{
            User user = userRep.findByUserSeq(userSeq);
            if (user == null) {
                throw new RuntimeException("요약 정보 조회에 실패했습니다.");
            }

            int isPrivate = userRep.findByUserSeq(userSeq).getIsPrivate(); //0: false, 1:true
            if (currentUserDetails != null) { //로그인한 사용자일 때
                Long currentUserSeq = currentUserDetails.getUserSeq(); //로그인한 사용자 고유 번호
                if (!currentUserSeq.equals(userSeq) && isPrivate == 1) {  //로그인한 사용자와 조회할 사용자가 같지 않을 때
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("비공개 게시물입니다.");
                }
            } else if (isPrivate == 1) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("비공개 게시물입니다.");
            }

            List<SummaryListDTO> summaryList = summaryRep.findSummaryList(userSeq);
            if(summaryList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 비었습니다.");
            }
            return ResponseEntity.ok().body(summaryList);
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 요약 정보 공개 옵션 변경  API
     *
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq            : 공개여부 변경할 사용자의 고유 번호
     * @param isPrivate          : 공개여부 - 0: 공개 1: 비공개
     * @author 이지은
     * @exception RuntimeException 500 공개 옵션 변경 실패
     * @exception RuntimeException 401 변경 권한이 없을 때
     * @return 요약 정보 공개 옵션 변경
     * */
    @Override
    public ResponseEntity<?> changeIsPrivate(PrincipalUserDetails currentUserDetails, Long userSeq, int isPrivate){
        try{
            User user = userRep.findByUserSeq(userSeq);
            if (user == null) {
                throw new RuntimeException("사용지 조회에 실패했습니다.");
            }

            if(currentUserDetails != null){ //로그인한 사용자일 때
                Long currentUserSeq = currentUserDetails.getUserSeq();
                if(!currentUserSeq.equals(userSeq)){ //다른 사용자일 때
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED) // 401 Error
                            .body("공개여부 설정을 변경할 수 있는 권한이 없습니다.");
                }
                if(isPrivate!=0 && isPrivate != 1) {
                    throw new RuntimeException("잘못된 요청입니다.");
                }
                if(isPrivate==0) user.setIsPrivate(0); //공개 설정
                else user.setIsPrivate(1); //비공개 설정

                userRep.save(user);
                return ResponseEntity.ok().body("공개 설정 변경에 성공했습니다.");
            } else {
                throw new RuntimeException("잘못된 정보입니다.");
            }
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
