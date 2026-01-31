package com.training.mybank.service;

import com.training.mybank.entities.*;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.repositories.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class AdminService {

    private final EntityManagerFactory emf;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AdminService(EntityManagerFactory emf,
                        UserRepository userRepository,
                        AccountRepository accountRepository,
                        TransactionRepository transactionRepository) {
        this.emf = emf;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /* -------- INTERNAL ADMIN CHECK -------- */

    private UserEntity validateAdmin(EntityManager em, String adminUsername) {
        UserEntity admin = userRepository.findByUsername(em, adminUsername);

        if (admin.getRole() != Role.ADMIN) {
            throw new BankingException("Access denied: Admin privileges required");
        }

        return admin;
    }

    /* -------- DELETE USER (SOFT DELETE) -------- */

    public void deleteUser(String adminUsername, String targetUsername) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            validateAdmin(em, adminUsername); // ✅ FIX

            UserEntity target =
                    userRepository.findOptionalByUsername(em, targetUsername);

            if (target == null)
                throw new BankingException("User not found");

            if (adminUsername.equals(targetUsername))
                throw new BankingException("Admin cannot delete himself");

            if (target.getRole() == Role.ADMIN)
                throw new BankingException("Cannot delete another admin");

            target.setIsActive(false);
            em.merge(target);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /* -------- LIST ALL USERS -------- */

    public List<UserEntity> listAllUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM UserEntity u ORDER BY u.createdAt DESC",
                    UserEntity.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    /* -------- USER TRANSACTIONS -------- */

    public List<TransactionEntity> getUserTransactions(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            UserEntity user = userRepository.findByUsername(em, username);
            return transactionRepository.findByUser(em, user);
        } finally {
            em.close();
        }
    }

    /* -------- ADD USER -------- */

    public void addUser(UserEntity user) {

        if (user.getRole() == null) {
            throw new BankingException("User role must be specified");
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /* -------- FREEZE / UNFREEZE ACCOUNT -------- */

    public void freezeAccount(Long accountId) {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            AccountEntity acc = em.find(AccountEntity.class, accountId);
            if (acc == null)
                throw new BankingException("Account not found");

            if (acc.getIsFrozen())
                throw new BankingException("Account already frozen");

            acc.setIsFrozen(true);
            em.merge(acc);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void unfreezeAccount(Long accountId) {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            AccountEntity acc = em.find(AccountEntity.class, accountId);
            if (acc == null)
                throw new BankingException("Account not found");

            if (!acc.getIsFrozen())
                throw new BankingException("Account is not frozen");

            acc.setIsFrozen(false);
            em.merge(acc);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<AccountEntity> listAllAccounts() {
        EntityManager em = emf.createEntityManager();
        try {
            return accountRepository.findAll(em);
        } finally {
            em.close();
        }
    }
}
