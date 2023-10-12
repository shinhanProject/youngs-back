package com.youngs.service;

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
}
