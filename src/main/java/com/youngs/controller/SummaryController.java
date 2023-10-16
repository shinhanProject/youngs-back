package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.SummaryDTO;
import com.youngs.security.PrincipalUserDetails;
import com.youngs.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return - 500 : 요약 작성 실패
     **/
    @PostMapping()
    public ResponseEntity<?> writeSummary(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestBody SummaryDTO summaryDTO) {
        if (summaryDTO.getCategory().equals("basic")) { // 기초 지식 요약 작성
            return summaryService.writeBasicSummary(currentUserDetails.getUserSeq(), summaryDTO);
        }
        else if (summaryDTO.getCategory().equals("news")) { // 보도자료 요약 작성
            return summaryService.writeNewsSummary(currentUserDetails.getUserSeq(), summaryDTO);
        }

        // 요약을 작성할 수 있는 카테고리의 글이 아닐 경우
        ResponseDTO responseDTO = ResponseDTO.builder().message("요약을 작성할 카테고리가 잘못되었습니다.").build();
        return ResponseEntity
                .internalServerError() // Error 500
                .body(responseDTO);
    }
}
