package com.youngs.controller;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserProfileDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.exception.NoChangeException;
import com.youngs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * 사용자 프로필 정보 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @return 사용자 프로필 정보
     * */
    @GetMapping("/{userSeq}")
    public ResponseEntity<?> searchProfile(@PathVariable Long userSeq){
        try{
            //사용자 인덱스에 해당하는 프로필 정보 조회
            UserProfileDTO userProfile = myPageService.searchUserByUserSeq(userSeq);
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
     * @author 이지은
     * @param userSeq 프로필 변경할 사용자 인덱스
     * @param request 사용자가 변경할 프로필 이미지와 닉네임
     * @return 프로필 변경 성공했을 떄(200)/변경사항이 없을 때(204)/변경헤 실패했을 때(500)
     * */
    @PatchMapping("/{userSeq}")
    public ResponseEntity<?> changeProfile(@PathVariable Long userSeq, @RequestBody Map<String, String> request){
        try{
            String profile = request.get("profile"); //변경할 프로팔
            String nickname = request.get("nickname"); //변경할 닉네임

            //사용자 프로필 및 닉네임 변경
            myPageService.changeProfile(userSeq, profile, nickname);
            return ResponseEntity.ok().body("프로필 변경에 성공했습니다.");
        } catch(NoChangeException e){ //변경 사항이 없을 떄
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT) // 204
                    .body(e.getMessage());
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
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

}
