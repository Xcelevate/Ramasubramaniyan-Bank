package com.training.mybank.service;

import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.AccountInactiveException;
import com.training.mybank.exceptions.AuthenticationFailedException;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AuthService {

    private final EntityManagerFactory emf;
    private final UserDAO userDAO;

    public AuthService(EntityManagerFactory emf, UserDAO userDAO) {
        this.emf = emf;
        this.userDAO = userDAO;
    }

    public String login(String username, String password) {

        EntityManager em = emf.createEntityManager();

        try {
            UserEntity user = userDAO.findByUsername(em, username);

            if (!PasswordUtil.matches(password, user.getPassword())) {
                throw new AuthenticationFailedException(
                        "Invalid username or password"
                );
            }

            if (!user.getIsActive()) {
                throw new AccountInactiveException("Account is inactive");
            }

            return username;

        } catch (BankingException e) {
            // domain exception → rethrow
            throw e;

        } catch (Exception e) {
            // unexpected failure
            throw new AuthenticationFailedException(
                    "Authentication failed"
            );

        } finally {
            em.close();
        }
    }
}
