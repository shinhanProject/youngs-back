package com.youngs.service;

import com.youngs.entity.User;

public interface AuthService {
    public boolean checkEmail(final String email);

    public User create(final User user);
}