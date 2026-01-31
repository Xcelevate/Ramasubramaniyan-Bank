package com.training.mybank.repositories;

import com.training.mybank.entities.UserEntity;
import com.training.mybank.exceptions.BankingException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserRepository {

    /* ---------- READ OPERATIONS ---------- */

    // STRICT – throws exception
    public UserEntity findByUsername(EntityManager em, String username) {
        try {
            return em.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.username = :username",
                            UserEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new BankingException(
                    "User not found with username: " + username
            );
        }
    }

    // SAFE – returns null
    public UserEntity findOptionalByUsername(EntityManager em, String username) {
        try {
            return em.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.username = :username",
                            UserEntity.class
                    ).setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public UserEntity findByUsernameAndEmail(
            EntityManager em, String username, String email) {

        try {
            return em.createQuery(
                            "SELECT u FROM UserEntity u " +
                                    "WHERE u.username = :u AND u.email = :e",
                            UserEntity.class
                    ).setParameter("u", username)
                    .setParameter("e", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new BankingException(
                    "User not found with username: " + username +
                            " and email: " + email
            );
        }
    }

    /* ---------- WRITE OPERATIONS ---------- */

    public void save(EntityManager em, UserEntity user) {
        em.persist(user);
    }

    // SOFT DELETE
    public void deactivate(EntityManager em, UserEntity user) {
        user.setIsActive(false);
        em.merge(user);
    }
}
