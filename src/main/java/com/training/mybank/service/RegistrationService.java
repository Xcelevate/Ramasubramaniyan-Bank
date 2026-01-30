package com.training.mybank.service;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.util.AccountNumberGenerator;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RegistrationService {

    private final EntityManagerFactory emf;
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;

    public RegistrationService(EntityManagerFactory emf,
                               UserDAO userDAO,
                               AccountDAO accountDAO) {
        this.emf = emf;
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    public String register(String username,
                           String password,
                           String fullName,
                           String email) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Check if user already exists
            if (accountDAO.existsByUsername(em, username)) {
                throw new BankingException("Username already exists");
            }

            // Create user
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(PasswordUtil.hash(password));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setIsActive(true);

            userDAO.save(em, user);

            // Create account
            String accountNumber = AccountNumberGenerator.generate(em);

            AccountEntity account = new AccountEntity();
            account.setUsername(username);
            account.setAccountNumber(accountNumber);
            account.setBalance(0.0);

            accountDAO.save(em, account);

            em.getTransaction().commit();

            return accountNumber;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
