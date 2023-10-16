package com.youngs.service;

import com.youngs.entity.User;
import com.youngs.security.PrincipalUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    ResponseEntity<?> reissueToken(String email, String refreshToken);
}
