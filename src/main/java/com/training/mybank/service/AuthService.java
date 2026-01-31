package com.training.mybank.service;

import com.training.mybank.repositories.UserRepository;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.AccountInactiveException;
import com.training.mybank.exceptions.AuthenticationFailedException;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AuthService {

    private final EntityManagerFactory emf;
    private final UserRepository userRepository;

    public AuthService(EntityManagerFactory emf, UserRepository userRepository) {
        this.emf = emf;
        this.userRepository = userRepository;
    }

    public UserEntity login(String username, String password) {

        EntityManager em = emf.createEntityManager();

        try {
            UserEntity user = userRepository.findByUsername(em, username);

            if (user == null) {
                throw new AuthenticationFailedException(
                        "Invalid username or password"
                );
            }

            if (!PasswordUtil.matches(password, user.getPassword())) {
                throw new AuthenticationFailedException(
                        "Invalid username or password"
                );
            }

            if (!user.getIsActive()) {
                throw new AccountInactiveException("Account is inactive");
            }

            if (user.getRole() == null) {
                throw new AuthenticationFailedException(
                        "User role not assigned"
                );
            }

            return user;

        } catch (BankingException e) {
            throw e;

        } catch (Exception e) {
            throw new AuthenticationFailedException(
                    "Authentication failed"
            );

        } finally {
            em.close();
        }
    }
}
