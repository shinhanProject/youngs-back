package com.youngs.service;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserDTO;
import com.youngs.entity.User;
import com.youngs.exception.RefreshTokenException;
import com.youngs.repository.UserRepository;
import com.youngs.security.PrincipalUserDetails;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
     * 로그아웃
     * @author : 박상희
     * @param currentUserDetails : 로그인한 사용자 정보
     * @return - 200 : 로그아웃 성공
     * @return - 500 : 로그아웃 실패
     **/
    @Override
    public ResponseEntity<?> logout(PrincipalUserDetails currentUserDetails) {
        if (currentUserDetails != null) { // 현재 로그인한 사용자가 있을 경우
            return ResponseEntity.ok().body("로그아웃에 성공했습니다.");
        }
        else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .message("로그아웃에 실패했습니다.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * Access Token이 만료되었으나 Refresh Token이 유효할 경우, 새로운 Access Token과 Refresh Token을 생성하는 메서드
     * @author : 박상희
     * @param response : HTTP 응답을 조작하기 위한 HttpServletResponse 객체
     * @param email : 사용자 이메일
     * @param refreshToken : 사용자의 현재 Refresh Token
     * @return - 토큰 재발급 성공 시 : 200, Access Token
     * @return - 토큰 재발급 실패 시 : 유효하지 않은 Refresh Token일 경우 400, 사용자 이메일 정보가 유효하지 않을 경우 500
     */
    @Override
    public ResponseEntity<?> reissueToken(HttpServletResponse response, String email, String refreshToken) {
        try {
            if (refreshToken != null) { // 쿠키에 Refresh Token이 있을 경우
                if (email == null) {
                    throw new RuntimeException("이메일 정보가 유효하지 않습니다."); // Error 500
                }

                User user = userRepository.findByEmail(email) // 토큰 재발급을 요청한 사용자
                        .orElseThrow(() -> new RuntimeException("회원 정보를 찾지 못했습니다.")); // Error 500

                if (!user.getRefreshToken().equals(refreshToken)) { // 쿠키에 저장된 Refresh Token이 DB에 저장된 Refresh Token과 다를 경우
                    throw new RefreshTokenException("Refresh Token이 다릅니다."); // Error 400
                }

                if (!tokenProvider.validateToken(refreshToken, "RefreshToken")) { // Refresh Token이 유효하지 않을 경우
                    throw new RefreshTokenException("Refresh Token 정보가 유효하지 않습니다."); // Error 400
                }

                // Refresh Token이 유효할 경우
                deleteCookie(response); // 기존의 쿠키 삭제

                UserDetails userDetails = principalUserDetailsService.loadUserByUsername(email); // 사용자 이메일 기준으로 사용자 인증 정보 가져오기
                String newAccessToken = tokenProvider.createAccessToken(userDetails); // 새로운 Access Token 생성
                String newRefreshToken = tokenProvider.createRefreshToken(); // 새로운 Refresh Token 생성

                user.setRefreshToken(newRefreshToken); // 사용자의 Refresh Token 새로운 Refresh Token으로 설정
                userRepository.save(user); // 새로운 Refresh Token DB에 저장

                createCookie(response, newRefreshToken); // 새로운 쿠키 생성

                final UserDTO responseUserDTO = UserDTO.builder()
                        .accessToken(newAccessToken) // 새로운 Access Token 반환
                        .build();

                return ResponseEntity.ok().body(responseUserDTO);
            }
            else { // 쿠키에 Refresh Token이 없을 경우
                throw new RuntimeException("쿠키에서 Refresh Token을 찾을 수 없습니다.");
            }
        }
        catch (RefreshTokenException re) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message(re.getMessage())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error 400
                    .body(responseDTO);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    /**
     * 쿠키를 생성하여 쿠키에 Refresh Token을 저장하고 클라이언트에 전달하는 메서드
     * @author : 박상희
     * @param response : HTTP 응답을 조작하기 위한 HttpServletResponse 객체
     * @param refreshToken : 사용자의 현재 Refresh Token
     **/
    public void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie newCookie = new Cookie("refreshToken", refreshToken); // 쿠키 생성
        newCookie.setMaxAge(30 * 24 * 60 * 60); // 쿠키 유효 시간 30 일

        // 쿠키 옵션 설정
        newCookie.setHttpOnly(true); // JavaScript에서 쿠키 접근 불가
        newCookie.setPath("/"); // 쿠키의 경로 설정

        response.addCookie(newCookie); // 쿠키를 클라이언트로 전송
    }

    /**
     * 클라이언트의 쿠키에서 Refresh Token을 삭제하는 메서드
     * @author : 박상희
     * @param response : HTTP 응답을 조작하기 위한 HttpServletResponse 객체
     **/
    public void deleteCookie(HttpServletResponse response) {
        Cookie deleteCookie = new Cookie("refreshToken", null); // 쿠키를 빈 값으로 설정하여 삭제

        deleteCookie.setMaxAge(0); // 쿠키 만료 시간을 0으로 설정하여 삭제

        response.addCookie(deleteCookie); // 쿠키를 클라이언트로 전송
    }
}
