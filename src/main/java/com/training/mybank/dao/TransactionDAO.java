package com.training.mybank.dao;

import com.training.mybank.entities.TransactionEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionDAO {

    private final EntityManager em;

    public TransactionDAO(EntityManager em) {
        this.em = em;
    }

    public void save(TransactionEntity tx) {
        em.persist(tx);
    }

    public List<TransactionEntity> findByAccountId(Long accountId) {
        return em.createQuery(
                        "SELECT t FROM TransactionEntity t " +
                                "WHERE t.fromAccount.id = :id OR t.toAccount.id = :id " +
                                "ORDER BY t.createdAt ASC",
                        TransactionEntity.class
                ).setParameter("id", accountId)
                .getResultList();
    }
}
