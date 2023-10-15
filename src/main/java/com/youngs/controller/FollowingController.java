package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.service.FollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/following")
public class FollowingController {
    private final FollowingService followingService;

    /**
     * 팔로우 하기
     * @author 이지은
     * @param request 팔로우할 타겟의 인덱스
     * @return 팔로우 여부 전달
     * */
    @PostMapping(value = "/follow")
    public ResponseEntity<?> addFollowing(@RequestBody Map<String, Long> request){
        try{
            Long userSeq = 1L; //로그인한 사용자 - 테스트용, 로그인이 생기면 토큰을 넣어서 테스트 진행 예정
            Long targetUserSeq = request.get("targetUserSeq"); //팔로잉할 유저

            // 로그인 하지 않은 사용자인지 체크 - 로그인 구현이 되었을 떄 할 수 있을 듯?

            // 로그인한 사용자와 팔로우 대상자가 다르다면
            if(!userSeq.equals(targetUserSeq)){
                followingService.saveFollowing(userSeq, targetUserSeq);
            } else{
                throw new RuntimeException("팔로우 대상자가 아닙니다.");
            }
            return ResponseEntity.ok().body("팔로우에 성공했습니다.");
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
