package com.training.mybank.dao;

import com.training.mybank.entities.TransactionEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionDAO {

    /* ---------- WRITE OPERATION ---------- */

    public void save(EntityManager em, TransactionEntity tx) {
        em.persist(tx);
    }


    /* ---------- READ OPERATION ---------- */

    public List<TransactionEntity> findByAccountId(EntityManager em, Long accountId) {
        return em.createQuery(
                        "SELECT t FROM TransactionEntity t " +
                                "WHERE t.fromAccount.id = :id OR t.toAccount.id = :id " +
                                "ORDER BY t.createdAt ASC",
                        TransactionEntity.class
                ).setParameter("id", accountId)
                .getResultList();
    }
}
