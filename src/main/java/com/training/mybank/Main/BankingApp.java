package com.training.mybank.Main;

import com.training.mybank.menu.MenuUI;
import com.training.mybank.repositories.UserRepository;
import com.training.mybank.util.AdminSeeder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BankingApp {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("bankPU");

        UserRepository userRepository = new UserRepository();

        // ✅ THIS CREATES ADMIN AFTER TRUNCATE
        AdminSeeder.seed(emf, userRepository);

        MenuUI menuUI = new MenuUI();
        menuUI.start();
    }
}
