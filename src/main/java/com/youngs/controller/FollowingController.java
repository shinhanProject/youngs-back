package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.security.PrincipalUserDetails;
import com.youngs.service.FollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/following")
public class FollowingController {
    private final FollowingService followingService;

    /**
     * 팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param request : 팔로우할 사용자의 고유 번호
     * @return - 200 : 팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 팔로우 실패
     **/
    @PostMapping("/follow")
    public ResponseEntity<?> addFollowing(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody Map<String, Long> request){
        try {
            Long userSeq = currentUserDetails.getUserSeq(); // 로그인한 사용자의 고유 번호
            Long targetUserSeq = request.get("targetUserSeq"); // 팔로우할 사용자

            if (!userSeq.equals(targetUserSeq)) { // 로그인한 사용자와 팔로우 대상자가 다를 경우
                followingService.saveFollowing(userSeq, targetUserSeq);
            }
            else{
                throw new RuntimeException("팔로우 대상자가 아닙니다.");
            }

            return ResponseEntity.ok().body("팔로우에 성공했습니다.");
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message("팔로우에 실패했습니다. " + e.getMessage()).build();

            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 언팔로우 하기
     * @author 이지은
     * @param request 언팔로우 타겟의 인덱스
     * @return 언팔로우 여부 전달
     * */
    @DeleteMapping("/unfollow")
    public ResponseEntity<?> deleteFollowing(@RequestBody Map<String, Long> request){
        try{
            Long userSeq = 1L; //로그인한 사용자 - 테스트용, 로그인이 생기면 토큰을 넣어서 테스트 진행 예정
            Long targetUserSeq = request.get("targetUserSeq"); //언팔로우 할 유저

            // 로그인 하지 않은 사용자인지 체크 - 로그인 구현이 되었을 떄 할 수 있을 듯?

            // 로그인한 사용자와 언팔로우 대상자가 다르다면
            if(!userSeq.equals(targetUserSeq)){
                followingService.deleteFollowing(userSeq, targetUserSeq);
            } else{
                throw new RuntimeException("언팔로우 대상자가 아닙니다.");
            }
            return ResponseEntity.ok().body("언팔로우에 성공했습니다.");
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
