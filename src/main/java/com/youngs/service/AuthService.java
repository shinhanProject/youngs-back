package com.youngs.service;

import com.youngs.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface AuthService {
    public boolean checkEmail(final String email);

    public User create(final User user);

    public User getByCredentials(final String email, final String userPw, final PasswordEncoder encoder);
}
