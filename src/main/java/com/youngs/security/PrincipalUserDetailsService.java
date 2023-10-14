package com.youngs.security;

import com.youngs.entity.User;
import com.youngs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 UserDetailsService를 구현한 클래스
 * @author : 박상희
 **/
@Service
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * 사용자의 이메일을 기준으로 데이터베이스에서 사용자 정보를 검색하여 UserDetails 객체로 반환하는 메서드
     * @author : 박상희
     * @param email the username identifying the user whose data is required. : 사용자 이메일
     * @return UserDetails 객체(PrincipalUserDetails)
     * @throws UsernameNotFoundException 사용자 정보를 찾지 못하는 경우 발생하는 예외
     **/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾지 못했습니다."));

        return PrincipalUserDetails.of(user);
    }
}
