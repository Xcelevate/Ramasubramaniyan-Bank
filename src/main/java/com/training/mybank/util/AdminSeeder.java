package com.training.mybank.util;

import com.training.mybank.entities.Role;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AdminSeeder {

    public static void seed(EntityManagerFactory emf,
                            UserRepository userRepository) {

        EntityManager em = emf.createEntityManager();

        try {
            // ✅ SAFE CHECK (NO EXCEPTION)
            UserEntity existing =
                    userRepository.findOptionalByUsername(em, "admin");

            if (existing != null) {
                return;
            }

            em.getTransaction().begin();

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(PasswordUtil.hash("admin@123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@mybank.com");
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);

            em.persist(admin);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}
