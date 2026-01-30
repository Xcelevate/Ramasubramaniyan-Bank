package com.training.mybank.repositories;

import com.training.mybank.entities.TransactionEntity;
import com.training.mybank.entities.UserEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionRepository {

    /* -------- SAVE -------- */

    public void save(EntityManager em, TransactionEntity tx) {
        em.persist(tx);
    }

    /* -------- USER TRANSACTIONS (ADMIN) -------- */

    public List<TransactionEntity> findByUser(EntityManager em, UserEntity user) {

        return em.createQuery(
                        "SELECT t FROM TransactionEntity t " +
                                "WHERE t.fromAccount.user = :user " +
                                "   OR t.toAccount.user = :user " +
                                "ORDER BY t.createdAt DESC",
                        TransactionEntity.class
                ).setParameter("user", user)
                .getResultList();
    }

    /* -------- ACCOUNT TRANSACTIONS (USER) -------- */

    public List<TransactionEntity> findByAccountId(EntityManager em, Long accountId) {

        return em.createQuery(
                        "SELECT t FROM TransactionEntity t " +
                                "WHERE t.fromAccount.id = :id " +
                                "   OR t.toAccount.id = :id " +
                                "ORDER BY t.createdAt DESC",
                        TransactionEntity.class
                ).setParameter("id", accountId)
                .getResultList();
    }
}
