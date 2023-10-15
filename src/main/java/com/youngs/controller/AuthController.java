package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserDTO;
import com.youngs.entity.User;
import com.youngs.repository.UserRepository;
import com.youngs.security.PrincipalUserDetailsService;
import com.youngs.security.TokenProvider;
import com.youngs.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;

    private final PrincipalUserDetailsService principalUserDetailsService;

    private final AuthService authService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
     * @return - 회원 가입 성공 시 : 200
     * @return - 회원 가입 실패 시 : 500
     **/
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 요청을 이용해 저장할 사용자 만들기
            User user = User.builder()
                    .email(userDTO.getEmail()) // 사용자 이메일
                    .nickname(userDTO.getNickname()) // 사용자 닉네임
                    .userPw(passwordEncoder.encode(userDTO.getUserPw())) // 사용자 비밀번호
                    .age(userDTO.getAge()) // 사용자 나이
                    .build();

            // 서비스를 이용해 리포지터리에 사용자 저장
            User registeredUser = authService.create(user);

            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message(registeredUser.getEmail() + " 회원 가입에 성공했습니다.")
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("회원 가입에 실패했습니다. " + e.getMessage())
                    .build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    /**
     * 로그인 메서드
     * @author : 박상희
     * @param userDTO : 사용자가 입력한 사용자 로그인 정보
     * @return - 로그인 성공 시 : 200, Access Token과 Refresh Token
     * @return - 로그인 실패 시 : 500
     **/
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        User user = authService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getUserPw(),
                passwordEncoder);

        if(user != null) {
            UserDetails principalUserDetails = principalUserDetailsService.loadUserByUsername(user.getEmail());

            // 토큰 생성
            final String accessToken = tokenProvider.createAccessToken(principalUserDetails);
            final String refreshToken = tokenProvider.createRefreshToken();

            user.setRefreshToken(refreshToken);
            userRepository.save(user); // 사용자 Refresh Token DB에 설정

            final UserDTO responseUserDTO = UserDTO.builder()
                    .userSeq(user.getUserSeq())
                    .nickname(user.getNickname())
                    .profile(user.getProfile())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        }
        else {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("로그인에 실패했습니다.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * 토큰 재발급 메서드
     * @author : 박상희
     * @param userDTO : 사용자 이메일과 사용자 Refresh Token이 포함된 사용자 정보
     * @return - 토큰 재발급 성공 시 : 200, Access Token과 Refresh Token
     * @return - 토큰 재발급 실패 시 : 유효하지 않은 Refresh Token일 경우 400, 사용자 이메일 정보가 유효하지 않을 경우 500
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(@RequestBody UserDTO userDTO) {
        return authService.reissueToken(userDTO.getEmail(), userDTO.getRefreshToken());
    }
}
