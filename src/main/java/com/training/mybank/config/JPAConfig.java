package com.training.mybank.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAConfig {
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");

    public JPAConfig() {
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }

    }
}
