package com.training.mybank.service;

import com.training.mybank.entities.Role;
import com.training.mybank.repositories.AccountRepository;
import com.training.mybank.repositories.UserRepository;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.util.AccountNumberGenerator;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RegistrationService {

    private final EntityManagerFactory emf;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public RegistrationService(EntityManagerFactory emf,
                               UserRepository userRepository,
                               AccountRepository accountRepository) {
        this.emf = emf;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public String register(String username,
                           String password,
                           String fullName,
                           String email) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Check if user already exists
            if (accountRepository.existsByUsername(em, username)) {
                throw new BankingException("Username already exists");
            }

            // Create user
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(PasswordUtil.hash(password));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setIsActive(true);
            if ("admin".equalsIgnoreCase(username)) {
                throw new BankingException("Username 'admin' is reserved");
            }

            user.setRole(Role.USER);

            userRepository.save(em, user);

            // Create account
            String accountNumber = AccountNumberGenerator.generate(em);

            AccountEntity account = new AccountEntity();
            account.setUsername(username);
            account.setAccountNumber(accountNumber);
            account.setBalance(0.0);

            accountRepository.save(em, account);

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
