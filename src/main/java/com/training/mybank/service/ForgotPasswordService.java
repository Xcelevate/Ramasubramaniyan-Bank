package com.training.mybank.service;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.InvalidRecoveryDetailsException;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;

public class ForgotPasswordService {

    private final EntityManager em;
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;

    public ForgotPasswordService(EntityManager em,
                                 UserDAO userDAO,
                                 AccountDAO accountDAO) {
        this.em = em;
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    /* ---------- SINGLE VERIFICATION METHOD ---------- */

    private UserEntity verifyRecoveryDetails(
            String username,
            String email,
            String accountNumber) {

        UserEntity user =
                userDAO.findByUsernameAndEmail(username, email);

        if (user == null) {
            return null;
        }

        AccountEntity account =
                accountDAO.findByUsernameAndAccountNumber(username, accountNumber);

        if (account == null) {
            return null;
        }

        return user;
    }

    /* ---------- RESET PASSWORD ---------- */

    public void resetPassword(String username,
                              String email,
                              String accountNumber,
                              String newPassword,
                              String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidRecoveryDetailsException(
                    "Passwords do not match");
        }

        UserEntity user =
                verifyRecoveryDetails(username, email, accountNumber);

        if (user == null) {
            throw new InvalidRecoveryDetailsException(
                    "Invalid username, email, or account number");
        }

        em.getTransaction().begin();

        user.setPassword(PasswordUtil.hash(newPassword));
        em.merge(user);

        em.getTransaction().commit();
    }
}
