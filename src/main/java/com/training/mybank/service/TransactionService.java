package com.training.mybank.service;

import com.training.mybank.repositories.AccountRepository;
import com.training.mybank.repositories.TransactionRepository;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.TransactionEntity;
import com.training.mybank.exceptions.InsufficientBalanceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class TransactionService {

    private final EntityManagerFactory emf;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(EntityManagerFactory emf,
                              AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.emf = emf;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /* -------- DEPOSIT -------- */

    public void deposit(String username, double amount) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }

            AccountEntity account = accountRepository.findByUsername(em, username);
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);

            TransactionEntity tx = new TransactionEntity();
            tx.setToAccount(account);
            tx.setTransactionType("DEPOSIT");
            tx.setAmount(amount);
            tx.setBalanceAfter(newBalance);
            tx.setRemarks("Deposit");

            em.merge(account);
            transactionRepository.save(em, tx);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close(); // ✅ CRITICAL
        }
    }

    /* -------- WITHDRAW -------- */

    public void withdraw(String username, double amount) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            if (amount <= 0) {
                throw new IllegalArgumentException("Withdraw amount must be positive");
            }

            AccountEntity account = accountRepository.findByUsername(em, username);

            if (account.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);

            TransactionEntity tx = new TransactionEntity();
            tx.setFromAccount(account);
            tx.setTransactionType("WITHDRAW");
            tx.setAmount(amount);
            tx.setBalanceAfter(newBalance);
            tx.setRemarks("Withdraw");

            em.merge(account);
            transactionRepository.save(em, tx);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /* -------- TRANSFER -------- */

    public void transfer(String fromUser, String toUser, double amount) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }

            AccountEntity from = accountRepository.findByUsername(em, fromUser);
            AccountEntity to = accountRepository.findByUsername(em, toUser);

            if (from.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance for transfer");
            }

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            TransactionEntity tx = new TransactionEntity();
            tx.setFromAccount(from);
            tx.setToAccount(to);
            tx.setTransactionType("TRANSFER");
            tx.setAmount(amount);
            tx.setBalanceAfter(from.getBalance());
            tx.setRemarks("Transfer from " + fromUser + " to " + toUser);

            em.merge(from);
            em.merge(to);
            transactionRepository.save(em, tx);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /* -------- BALANCE -------- */

    public double checkBalance(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            return accountRepository.findByUsername(em, username).getBalance();
        } finally {
            em.close();
        }
    }

    /* -------- TRANSACTION HISTORY -------- */

    public List<TransactionEntity> getTransactionHistory(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            AccountEntity account = accountRepository.findByUsername(em, username);
            return transactionRepository.findByAccountId(em, account.getId());
        } finally {
            em.close();
        }
    }
}
