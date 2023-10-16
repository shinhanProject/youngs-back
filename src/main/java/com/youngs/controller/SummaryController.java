package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.SummaryDTO;
import com.youngs.security.PrincipalUserDetails;
import com.youngs.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/summary")
public class SummaryController {
    private final SummaryService summaryService;

    /**
     * 요약 작성
     * @author : 박상희
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 요약 작성 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 작성 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 작성 실패
     **/
    @PostMapping()
    public ResponseEntity<?> writeSummary(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody SummaryDTO summaryDTO) {
        String category = summaryDTO.getCategory(); // 요약을 작성할 카테고리

        if (category.equals("basic")) { // 기초 지식 요약 작성
            return summaryService.writeBasicSummary(currentUserDetails.getUserSeq(), summaryDTO);
        }
        else if (category.equals("news")) { // 보도자료 요약 작성
            return summaryService.writeNewsSummary(currentUserDetails.getUserSeq(), summaryDTO);
        }

        // 요약을 작성할 수 있는 카테고리의 글이 아닐 경우
        ResponseDTO responseDTO = ResponseDTO.builder().message("요약을 작성할 카테고리가 잘못되었습니다.").build();
        return ResponseEntity
                .internalServerError() // Error 500
                .body(responseDTO);
    }

    /**
     * 요약 수정
     * @author : 박상희
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param summaryDTO : 수정할 요약 정보
     * @return - 200 : 요약 수정 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 수정 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 수정 실패
     **/
    @PatchMapping()
    public ResponseEntity<?> editSummary(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody SummaryDTO summaryDTO) {
        return summaryService.editSummary(currentUserDetails.getUserSeq(), summaryDTO);
    }
}
