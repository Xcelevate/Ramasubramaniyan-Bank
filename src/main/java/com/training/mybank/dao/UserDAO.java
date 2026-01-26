package com.training.mybank.dao;

import com.training.mybank.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserDAO{
    private final EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }
    public UserEntity findByUsername(String username){
        try {
            return em.createQuery(
                    "select u from UserEntity u where u.username = :username", UserEntity.class).setParameter("username", username).getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
    public void save(UserEntity user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
    public UserEntity findByUsernameAndEmail(String username, String email) {
        try {
            return em.createQuery(
                            "SELECT u FROM UserEntity u WHERE u.username = :u AND u.email = :e",
                            UserEntity.class
                    ).setParameter("u", username)
                    .setParameter("e", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}