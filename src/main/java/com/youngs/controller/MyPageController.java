package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class MyPageController {
    private final MyPageService myPageService;

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
            List<UserSandDTO> userSandList = myPageService.searchuserSandList(userSeq);
            return ResponseEntity.ok().body(userSandList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
