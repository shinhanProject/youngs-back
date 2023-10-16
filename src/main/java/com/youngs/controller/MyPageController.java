package com.youngs.controller;

import com.youngs.dto.*;
import com.youngs.security.PrincipalUserDetails;
import com.youngs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * 사용자 프로필 정보 조회하는 메서드
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 조회할 사용자 고유 번호
     * @return 사용자 프로필 정보
     **/
    @GetMapping("/{userSeq}")
    public ResponseEntity<?> searchProfileByUser(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @PathVariable Long userSeq){
        try {
            // 사용자 인덱스에 해당하는 프로필 정보 조회
            UserProfileDTO userProfile = myPageService.searchUserByUserSeq(currentUserDetails, userSeq);

            return ResponseEntity.ok().body(userProfile);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 사용자 프로필 편집 API - 프로필 이미지 및 닉네임 변경
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 프로필 편집할 사용자의 고유 번호
     * @param request 사용자가 변경할 프로필 이미지와 닉네임
     * @return - 프로필 편집 성공 시 : 200
     * @return - 프로필 변경 사항이 없을 경우 : 204
     * @return - 로그인하지 않은 상태로 프로필 편집을 시도했을 경우 : 401
     * @return - 프로필 편집 실패 시 : 500
     */
    @PatchMapping("/{userSeq}")
    public ResponseEntity<?> changeProfile(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @PathVariable Long userSeq, @RequestBody Map<String, String> request){
        String profile = request.get("profile"); // 변경할 프로팔
        String nickname = request.get("nickname"); // 변경할 닉네임

        // 사용자 프로필 및 닉네임 변경
        return myPageService.changeProfile(currentUserDetails, userSeq, profile, nickname);
    }

    /**
     * 모래사장 조회 API
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @return 모래사장에 뿌릴 조개 리스트
     * */
    @GetMapping("/{userSeq}/sand")
    public ResponseEntity<?> searchUserSand(@PathVariable Long userSeq){
        try{
            //사용자 인덱스에 해당하는 모래사장 조회
            List<UserSandDTO> userSandList = myPageService.searchUserSandList(userSeq);
            return ResponseEntity.ok().body(userSandList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 팔로잉 목록 조회 API
     * @author 이지은
     * @param userSeq 조회할 사용자 인덱스
     * @return 팔로잉 리스트
     * */
    @GetMapping("/{userSeq}/following")
    public ResponseEntity<?> searchFollowing(@PathVariable Long userSeq){
        try{
            //사용자 인덱스에 해당하는 팔로잉 목록 조회
            List<FollowingDTO> followingDTOList = myPageService.sarchFollowingList(userSeq);
            return ResponseEntity.ok().body(followingDTOList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 팔로워 목록 조회 API
     * @author 이지은
     * @param userSeq 조회할 사용자 인덱스
     * @return 팔로잉 리스트
     * */
    @GetMapping("/{userSeq}/follower")
    public ResponseEntity<?> searchFollower(@PathVariable Long userSeq){
        try{
            //사용자 인덱스에 해당하는 팔로워 목록 조회
            List<FollowingDTO> followingDTOList = myPageService.sarchFollowerList(userSeq);
            return ResponseEntity.ok().body(followingDTOList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 사용자 요약 정보 최신 순 조회 API
     * @author 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param userSeq : 조회할 사용자 고유 번호
     * @return 사용자 요약 정보 리스트
     * */
    @GetMapping("/{userSeq}/summary")
    public ResponseEntity<?> searchSummary(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @PathVariable Long userSeq){
        try{
            //사용자 요약 정보 조회
            List<SummaryListDTO> summaryList = myPageService.searchSummary(currentUserDetails, userSeq);
            return ResponseEntity.ok().body(summaryList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

}
