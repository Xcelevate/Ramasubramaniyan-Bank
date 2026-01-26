package com.training.mybank.dao;

import com.training.mybank.entities.AccountEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class AccountDAO {

    private final EntityManager em;

    public AccountDAO(EntityManager em) {
        this.em = em;
    }

    // One user → one account
    public boolean existsByUsername(String username) {
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

    // Ensure account number uniqueness
    public boolean existsByAccountNumber(String accountNumber) {
        Long count = em.createQuery(
                        "SELECT COUNT(a) FROM AccountEntity a WHERE a.accountNumber = :acc",
                        Long.class
                ).setParameter("acc", accountNumber)
                .getSingleResult();

        return count > 0;
    }

    public void save(AccountEntity account) {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }

    public AccountEntity findByUsername(String username) {
        try {
            return em.createQuery(
                            "SELECT a FROM AccountEntity a WHERE a.username = :username",
                            AccountEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public void updateBalance(AccountEntity account,double newBalance){
        em.getTransaction().begin();
        account.setBalance(newBalance);
        em.merge(account);
        em.getTransaction().commit();
    }
    public AccountEntity findByUsernameAndAccountNumber(
            String username, String accountNumber) {

        try {
            return em.createQuery(
                            "SELECT a FROM AccountEntity a WHERE a.username = :u AND a.accountNumber = :acc",
                            AccountEntity.class
                    ).setParameter("u", username)
                    .setParameter("acc", accountNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public List<AccountEntity> findAll(){
        return em.createQuery("SELECT  A FROM AccountEntity A",
                AccountEntity.class).getResultList();
    }

}
