package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserDTO;
import com.youngs.entity.User;
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

    /**
     * 회원 가입 메서드
     * @author : 박상희
     * @param userDTO : 사용자가 입력한 사용자 회원 가입 정보
     * @return ResponseEntity
     **/
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 요청을 이용해 저장할 사용자 만들기
            User user = User.builder()
                    .email(userDTO.getEmail()) // 사용자 이메일
                    .nickname(userDTO.getNickname()) // 사용자 닉네임
                    .userPw(userDTO.getUserPw()) // 사용자 비밀번호
                    .age(userDTO.getAge()) // 사용자 나이
                    .build();

            // 서비스를 이용해 리포지터리에 사용자 저장
            User registeredUser = authService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .userSeq(registeredUser.getUserSeq()) // 사용자 고유 번호
                    .email(registeredUser.getEmail()) // 사용자 이메일
                    .nickname(registeredUser.getNickname()) // 사용자 닉네임
                    .age(userDTO.getAge()) // 사용자 나이
                    .build();

            // 사용자 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴
            return ResponseEntity.ok().body(responseUserDTO);
        }
        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().message(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    /**
     * 로그인 메서드
     * @author : 박상희
     * @param userDTO : 사용자가 입력한 사용자 로그인 정보
     * @return ResponseEntity
     **/
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        User user = authService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getUserPw());

        if(user != null) {
            final UserDTO responseUserDTO = UserDTO.builder()
                    .userSeq(user.getUserSeq())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .age(user.getAge())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        }
        else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("로그인에 실패했습니다.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }
}
