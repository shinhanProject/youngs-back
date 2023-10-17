package com.youngs.service;

import com.youngs.entity.User;
import com.youngs.security.PrincipalUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean checkEmail(final String email);

    User create(final User user);

    User getByCredentials(final String email, final String userPw, final PasswordEncoder encoder);

    /**
     * 로그아웃
     * @author : 박상희
     * @param currentUserDetails : 로그인한 사용자 정보
     * @return - 200 : 로그아웃 성공
     * @return - 500 : 로그아웃 실패
     **/
    ResponseEntity<?> logout(PrincipalUserDetails currentUserDetails);

    /**
     * Access Token이 만료되었으나 Refresh Token이 유효할 경우, 새로운 Access Token과 Refresh Token을 생성하는 메서드
     * @author : 박상희
     * @param response : HTTP 응답을 조작하기 위한 HttpServletResponse 객체
     * @param email : 사용자 이메일
     * @param refreshToken : 사용자의 현재 Refresh Token
     * @return - 토큰 재발급 성공 시 : 200, Access Token
     * @return - 토큰 재발급 실패 시 : 유효하지 않은 Refresh Token일 경우 400, 사용자 이메일 정보가 유효하지 않을 경우 500
     **/
    ResponseEntity<?> reissueToken(HttpServletResponse response, String email, String refreshToken);
}
