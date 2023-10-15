package com.youngs.controller;

import com.youngs.dto.BasicArticleDTO;
import com.youngs.dto.ResponseDTO;
import com.youngs.security.PrincipalUserDetails;
import com.youngs.service.BasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/basic")
public class BasicController {
    private final BasicService basicService;

    /**
     * 기초 지식 조회 API
     * @author 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param categorySeq 기초자료 카테고리 인덱스. 1: 주식 기초, 2: 주식 투자 기법, 3: 위험성, 4: 경제 기초 지식
     */
    @GetMapping("/category")
    public ResponseEntity<?> searchArticleList(@AuthenticationPrincipal PrincipalUserDetails currentUserDetails, @RequestParam Long categorySeq, boolean isChecked){
        try{
            List<BasicArticleDTO> basicArticleList = basicService.searchBasicArticleList(currentUserDetails, categorySeq, isChecked);
            return ResponseEntity.ok().body(basicArticleList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
