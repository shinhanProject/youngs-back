package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 사용자가 입력한 사용자 이메일이 존재하는지 체크하는 메서드
     * @author : 박상희
     * @param emailMap : 사용자가 입력한 사용자 이메일
     * @return - 사용자가 입력한 사용자 이메일이 존재하지 않을 경우 : 200
     * @return - 사용자가 입력한 사용자 이메일이 존재할 경우 : 500
     **/
    @PostMapping("/emailcheck")
    public ResponseEntity<?> checkUserEmail(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");

        if (authService.checkEmail(email)) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("이메일이 중복되지 않습니다.")
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }
        else {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("이메일이 중복됩니다.")
                    .build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }
}
