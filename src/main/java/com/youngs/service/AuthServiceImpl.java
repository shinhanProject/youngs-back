package com.youngs.service;

import com.youngs.entity.User;
import com.youngs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

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
     */
    @Override
    public User create(final User user) {
        if (user == null || user.getEmail() == null || user.getNickname() == null || user.getUserPw() == null) {
            throw new RuntimeException("회원 가입에 실패했습니다.");
        }

        final String email = user.getEmail();
        System.out.println(email);
        System.out.println(checkEmail(email));
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
     * @return 검색된 사용자 객체
     */
    @Override
    public User getByCredentials(final String email, final String userPw) {
        return userRepository.findByEmailAndUserPw(email, userPw);
    }
}
