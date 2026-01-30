package com.training.mybank.service;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.InvalidRecoveryDetailsException;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ForgotPasswordService {

    private final EntityManagerFactory emf;
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;

    public ForgotPasswordService(EntityManagerFactory emf,
                                 UserDAO userDAO,
                                 AccountDAO accountDAO) {
        this.emf = emf;
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    /* ---------- VERIFY RECOVERY DETAILS ---------- */

    private UserEntity verifyRecoveryDetails(
            EntityManager em,
            String username,
            String email,
            String accountNumber) {

        UserEntity user =
                userDAO.findByUsernameAndEmail(em, username, email);

        AccountEntity account =
                accountDAO.findByUsernameAndAccountNumber(em, username, accountNumber);

        // If either lookup fails, DAO will throw exception
        return user;
    }

    /* ---------- RESET PASSWORD ---------- */

    public void resetPassword(String username,
                              String email,
                              String accountNumber,
                              String newPassword,
                              String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidRecoveryDetailsException("Passwords do not match");
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            UserEntity user = verifyRecoveryDetails(
                    em, username, email, accountNumber);

            user.setPassword(PasswordUtil.hash(newPassword));
            em.merge(user);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new InvalidRecoveryDetailsException(
                    "Invalid username, email, or account number"
            );
        } finally {
            em.close();
        }
    }
}
