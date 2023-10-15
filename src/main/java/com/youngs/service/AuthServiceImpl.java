package com.youngs.service;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserDTO;
import com.youngs.entity.User;
import com.youngs.repository.UserRepository;
import com.youngs.security.PrincipalUserDetailsService;
import com.youngs.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PrincipalUserDetailsService principalUserDetailsService;

    private final TokenProvider tokenProvider;

    /**
     * 사용자가 입력한 사용자 이메일이 존재하는지 검사하는 메서드
     * @author : 박상희
     * @param email : 사용자가 입력한 사용자 이메일
     * @return - 사용자가 입력한 사용자 이메일이 존재하지 않을 경우 : true
     * @return - 사용자가 입력한 사용자 이메일이 존재할 경우 : false
     **/
    @Override
    public boolean checkEmail(final String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * 새로운 사용자를 생성하는 메서드
     * @author : 박상희
     * @param user : 회원 가입할 사용자 객체
     * @return 등록할 사용자 객체
     * @throws RuntimeException 사용자 데이터가 유효하지 않을 경우 예외 발생
     **/
    @Override
    public User create(final User user) {
        if (user == null || user.getEmail() == null || user.getNickname() == null || user.getUserPw() == null) {
            throw new RuntimeException("회원 가입에 실패했습니다.");
        }

        final String email = user.getEmail();
        if (!checkEmail(email)) {
            log.warn("이메일이 이미 존재합니다. {}", email);
            throw new RuntimeException("이메일이 이미 존재합니다.");
        }

        return userRepository.save(user);
    }

    /**
     * 사용자 이메일과 사용자 비밀번호를 이용해서 사용자를 검색하는 메서드
     * @author : 박상희
     * @param email : 사용자 이메일
     * @param userPw : 사용자 비밀번호
     * @param encoder : 비밀번호 암호화를 위한 PasswordEncoder
     * @return 검색된 사용자 객체
     **/
    @Override
    public User getByCredentials(final String email, final String userPw, final PasswordEncoder encoder) {
        final User originalUser = userRepository.findByEmail(email).orElse(null);

        if (originalUser != null && encoder.matches(userPw, originalUser.getUserPw())) {
            return originalUser;
        }

        return null;
    }

    /**
     * Access Token이 만료되었으나 Refresh Token이 유효할 경우, 새로운 Access Token과 Refresh Token을 생성하는 메서드
     * @author : 박상희
     * @param email : 사용자 이메일
     * @param refreshToken : 사용자의 현재 Refresh Token
     * @return Access Token과 Refresh Token이 포함된 ResponseEntity
     **/
    @Override
    public ResponseEntity<?> reissueToken(String email, String refreshToken) {
        if (email == null) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("이메일 정보가 유효하지 않습니다.")
                    .build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("회원 정보를 찾지 못했습니다."));

        if (!user.getRefreshToken().equals(refreshToken)) { // 전달받은 Refresh Token이 DB에 저장된 Refresh Token과 다를 경우
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("Refresh Token이 다릅니다.")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error 400
                    .body(responseDTO);
        }

        if (!tokenProvider.validateToken(refreshToken, "RefreshToken")) { // Refresh Token이 유효하지 않을 경우
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message("Refresh Token 정보가 유효하지 않습니다.")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error 400
                    .body(responseDTO);
        }

        // Refresh Token이 유효할 경우
        UserDetails userDetails = principalUserDetailsService.loadUserByUsername(email); // 사용자 이메일 기준으로 사용자 인증 정보 가져오기
        String newAccessToken = tokenProvider.createAccessToken(userDetails); // 새로운 Access Token 생성
        String newRefreshToken = tokenProvider.createRefreshToken(); // 새로운 Refresh Token 생성

        user.setRefreshToken(newRefreshToken); // 새로운 Refresh Token DB에 저장

        final UserDTO responseUserDTO = UserDTO.builder()
                .accessToken(newAccessToken) // 새로운 Access Token 반환
                .refreshToken(newRefreshToken)
                .build();

        return ResponseEntity.ok().body(responseUserDTO);
    }
}
