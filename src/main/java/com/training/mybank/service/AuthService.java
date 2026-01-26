package com.training.mybank.service;

import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.AccountInactiveException;
import com.training.mybank.exceptions.AuthenticationFailedException;
import com.training.mybank.util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String login(String username, String password) {

        UserEntity user = userDAO.findByUsername(username);

        if (user == null ||
                !PasswordUtil.matches(password, user.getPassword())) {
            throw new AuthenticationFailedException(
                    "Invalid username or password");
        }

        if (!user.getIsActive()) {
            throw new AccountInactiveException("Account is inactive");
        }

        return username;
    }
}
