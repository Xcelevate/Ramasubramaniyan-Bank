package com.training.mybank.controller;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.entities.AccountEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Comparator;
import java.util.List;

public class AccountController {

    private final EntityManagerFactory emf;
    private final AccountDAO accountDAO;

    public AccountController(EntityManagerFactory emf, AccountDAO accountDAO) {
        this.emf = emf;
        this.accountDAO = accountDAO;
    }

    public void listAccountsSortedByAccountNumber() {

        EntityManager em = emf.createEntityManager();

        try {
            List<AccountEntity> accounts = accountDAO.findAll(em);

            if (accounts.isEmpty()) {
                System.out.println("\nNo accounts available.");
                return;
            }


            accounts.sort(
                    Comparator.comparingLong(
                            acc -> Long.parseLong(acc.getAccountNumber())
                    )
            );

            // 🔹 UI DISPLAY
            System.out.println("\n========== ACCOUNT LIST (SORTED BY ACCOUNT NUMBER) ==========");
            System.out.printf("%-5s %-15s %-15s %-20s%n",
                    "No", "Username", "Account No", "Created At");
            System.out.println("-------------------------------------------------------------");

            int i = 1;
            for (AccountEntity acc : accounts) {
                System.out.printf("%-5d %-15s %-15s %-20s%n",
                        i++,
                        acc.getUsername(),
                        acc.getAccountNumber(),
                        acc.getCreatedAt());
            }

            System.out.println("=============================================================");

        } finally {
            em.close();
        }
    }
}
