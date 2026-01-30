package com.training.mybank.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAConfig {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bankPU");

    private JPAConfig() {
        // prevent instantiation
    }

    /* -------- FACTORY ACCESS -------- */

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /* -------- SHUTDOWN -------- */

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
