package com.training.mybank.service;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.TransactionDAO;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.TransactionEntity;
import com.training.mybank.exceptions.InsufficientBalanceException;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionService {

    private final EntityManager em;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService(EntityManager em,
                              AccountDAO accountDAO,
                              TransactionDAO transactionDAO) {
        this.em = em;
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    /* -------- DEPOSIT -------- */

    public void deposit(String username, double amount) {

        em.getTransaction().begin();

        AccountEntity account = accountDAO.findByUsername(username);
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        TransactionEntity tx = new TransactionEntity();
        tx.setToAccount(account);
        tx.setTransactionType("DEPOSIT");
        tx.setAmount(amount);
        tx.setBalanceAfter(newBalance);

        em.merge(account);
        transactionDAO.save(tx);

        em.getTransaction().commit();
    }

    /* -------- WITHDRAW -------- */

    public void withdraw(String username, double amount) {

        em.getTransaction().begin();

        AccountEntity account = accountDAO.findByUsername(username);

        if (account.getBalance() < amount) {
            em.getTransaction().rollback();
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        TransactionEntity tx = new TransactionEntity();
        tx.setFromAccount(account);
        tx.setTransactionType("WITHDRAW");
        tx.setAmount(amount);
        tx.setBalanceAfter(newBalance);

        em.merge(account);
        transactionDAO.save(tx);

        em.getTransaction().commit();
    }

    /* -------- TRANSFER -------- */

    public void transfer(String fromUser, String toUser, double amount) {

        em.getTransaction().begin();

        AccountEntity from = accountDAO.findByUsername(fromUser);
        AccountEntity to = accountDAO.findByUsername(toUser);

        if (from.getBalance() < amount) {
            em.getTransaction().rollback();
            throw new InsufficientBalanceException(
                    "Insufficient balance for transfer");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        TransactionEntity tx = new TransactionEntity();
        tx.setFromAccount(from);
        tx.setToAccount(to);
        tx.setTransactionType("TRANSFER");
        tx.setAmount(amount);
        tx.setBalanceAfter(from.getBalance());

        em.merge(from);
        em.merge(to);
        transactionDAO.save(tx);

        em.getTransaction().commit();
    }

    public double checkBalance(String username) {
        return accountDAO.findByUsername(username).getBalance();
    }

    public List<TransactionEntity> getTransactionHistory(String username) {
        AccountEntity account = accountDAO.findByUsername(username);
        return transactionDAO.findByAccountId(account.getId());
    }
}
