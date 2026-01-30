package com.training.mybank.dao;

import com.training.mybank.entities.AccountEntity;
import com.training.mybank.exceptions.BankingException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class AccountDAO {

    /* ---------- READ OPERATIONS ---------- */

    public boolean existsByUsername(EntityManager em, String username) {
        try {
            em.createQuery(
                            "SELECT a FROM AccountEntity a WHERE a.username = :username",
                            AccountEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean existsByAccountNumber(EntityManager em, String accountNumber) {
        Long count = em.createQuery(
                        "SELECT COUNT(a) FROM AccountEntity a WHERE a.accountNumber = :acc",
                        Long.class
                ).setParameter("acc", accountNumber)
                .getSingleResult();

        return count > 0;
    }

    public AccountEntity findByUsername(EntityManager em, String username) {
        try {
            return em.createQuery(
                            "SELECT a FROM AccountEntity a WHERE a.username = :username",
                            AccountEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new BankingException(
                    "Account not found for username: " + username
            );
        }
    }

    public AccountEntity findByUsernameAndAccountNumber(
            EntityManager em, String username, String accountNumber) {

        try {
            return em.createQuery(
                            "SELECT a FROM AccountEntity a " +
                                    "WHERE a.username = :u AND a.accountNumber = :acc",
                            AccountEntity.class
                    ).setParameter("u", username)
                    .setParameter("acc", accountNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new BankingException(
                    "Account not found for username: " + username +
                            " and account number: " + accountNumber
            );
        }
    }

    public List<AccountEntity> findAll(EntityManager em) {
        return em.createQuery(
                "SELECT a FROM AccountEntity a",
                AccountEntity.class
        ).getResultList();
    }

    /* ---------- WRITE OPERATIONS ---------- */

    public void save(EntityManager em, AccountEntity account) {
        em.persist(account);
    }

    public AccountEntity update(EntityManager em, AccountEntity account) {
        return em.merge(account);
    }
}
