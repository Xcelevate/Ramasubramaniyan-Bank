package com.training.mybank.controller;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.entities.AccountEntity;

import java.util.List;

public class AccountController {

    private final AccountDAO accountDAO;

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void listAccountsSortedByAccountNumber() {

        List<AccountEntity> accounts = accountDAO.findAll();

        if (accounts == null || accounts.isEmpty()) {
            System.out.println("\nNo accounts available.");
            return;
        }

        // 🔹 DSA SORT (Bubble Sort)
        bubbleSortByAccountNumber(accounts);

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
    }

    /* ---------------- BUBBLE SORT (DSA) ---------------- */

    private void bubbleSortByAccountNumber(List<AccountEntity> accounts) {

        int n = accounts.size();

        for (int i = 0; i < n - 1; i++) {

            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                long accNo1 =
                        Long.parseLong(accounts.get(j).getAccountNumber());
                long accNo2 =
                        Long.parseLong(accounts.get(j + 1).getAccountNumber());

                if (accNo1 > accNo2) {
                    // swap
                    AccountEntity temp = accounts.get(j);
                    accounts.set(j, accounts.get(j + 1));
                    accounts.set(j + 1, temp);
                    swapped = true;
                }
            }

            // Optimization: stop if already sorted
            if (!swapped) {
                break;
            }
        }
    }
}
