package com.training.mybank.service;

import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.InvalidRecoveryDetailsException;
import com.training.mybank.repositories.AccountRepository;
import com.training.mybank.repositories.UserRepository;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ForgotPasswordService {

    private final EntityManagerFactory emf;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public ForgotPasswordService(EntityManagerFactory emf,
                                 UserRepository userRepository,
                                 AccountRepository accountRepository) {
        this.emf = emf;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    /* ---------- VERIFY RECOVERY DETAILS ---------- */

    private UserEntity verifyRecoveryDetails(EntityManager em,
                                             String username,
                                             String email,
                                             String accountNumber) {

        UserEntity user =
                userRepository.findByUsernameAndEmail(em, username, email);

        AccountEntity account =
                accountRepository.findByUsernameAndAccountNumber(
                        em, username, accountNumber
                );

        if (!user.getIsActive()) {
            throw new InvalidRecoveryDetailsException("User account is inactive");
        }

        if (account.getIsFrozen()) {
            throw new InvalidRecoveryDetailsException(
                    "Account is frozen. Password reset not allowed"
            );
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
            throw new InvalidRecoveryDetailsException("Passwords do not match");
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            UserEntity user = verifyRecoveryDetails(
                    em, username, email, accountNumber
            );

            user.setPassword(PasswordUtil.hash(newPassword));
            em.merge(user);

            em.getTransaction().commit();

        }
        catch (InvalidRecoveryDetailsException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // user mistake → rethrow cleanly
            throw e;

        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // real system failure
            throw new InvalidRecoveryDetailsException(
                    "Unable to reset password. Please verify your details and try again."
            );
        }
        finally {
            em.close();
        }
    }

}
