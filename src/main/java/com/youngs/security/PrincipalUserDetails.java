package com.youngs.security;

import com.youngs.entity.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security의 UserDetails 인터페이스에 사용자 정보를 매핑하는 클래스
 * 사용자 인증 및 권한 관리를 위해 사용된다.
 * @author : 박상희
 **/
@Builder
public class PrincipalUserDetails implements UserDetails {
    private Long userSeq; // 사용자 고유 번호
    private String email; // 사용자 이메일
    private String nickname; // 사용자 닉네임
    private String userPw; // 사용자 비밀번호
    private String profile; // 사용자 프로필 이미지 이름

    /**
     * 사용자의 권한을 반환하는 메서드
     * @author : 박상희
     * @return 사용자의 권한 목록
     **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 사용자 비밀번호를 반환하는 메서드
     * @author : 박상희
     * @return 사용자 비밀번호
     **/
    @Override
    public String getPassword() {
        return userPw;
    }

    /**
     * 사용자 이메일을 반환하는 메서드
     * @author : 박상희
     * @return 사용자 이메일
     **/
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 사용자 계정의 만료 여부를 반환하는 메서드 (true : 만료 X, false : 만료 O)
     * @author : 박상희
     * @return 사용자 계정 만료 여부
     **/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 사용자 계정의 잠금 여부를 반환하는 메서드 (true : 잠금 X, false : 잠금 O)
     * @author : 박상희
     * @return 사용자 계정 잠금 여부
     **/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자 자격 증명의 만료 여부를 반환하는 메서드 (true : 만료 X, false : 만료 O)
     * @author : 박상희
     * @return 사용자 자격 증명 만료 여부
     **/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 계정의 활성화 여부를 반환하는 메서드 (true : 활성화, false : 비활성화)
     * @author : 박상희
     * @return 사용자 계정 활성화 여부
     **/
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 사용자 고유 번호를 반환하는 메서드
     * @author : 박상희
     * @return 사용자 고유 번호
     **/
    public Long getUserSeq() {
        return userSeq;
    }

    /**
     * 사용자 객체로 PrincipalUserDetails 객체를 생성하고 반환하는 메서드
     * @author : 박상희
     * @param user : 사용자 객체
     * @return PrincipalUserDetails 객체
     **/
    public static PrincipalUserDetails of(User user) {
        return PrincipalUserDetails.builder()
                .userSeq(user.getUserSeq())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .build();
    }
}
