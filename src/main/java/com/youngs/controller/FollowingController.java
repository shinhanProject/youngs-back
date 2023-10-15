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
    public ResponseEntity<?> addFollowing(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody Map<String, Long> request) {
        Long currentUserSeq = currentUserDetails.getUserSeq(); // 로그인한 사용자의 고유 번호
        Long targetUserSeq = request.get("targetUserSeq");  // 팔로우할 사용자의 고유 번호

        return followingService.saveFollowing(currentUserSeq, targetUserSeq);
    }

    /**
     * 언팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param request : 언팔로우할 사용자의 고유 번호
     * @return - 200 : 언팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 언팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 언팔로우 실패
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<?> deleteFollowing(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody Map<String, Long> request) {
        Long currentUserSeq = currentUserDetails.getUserSeq(); // 로그인한 사용자의 고유 번호
        Long targetUserSeq = request.get("targetUserSeq"); // 언팔로우할 사용자의 고유 번호

        return followingService.deleteFollowing(currentUserSeq, targetUserSeq);
    }
}
