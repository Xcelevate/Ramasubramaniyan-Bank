package com.training.mybank.menu;

import com.training.mybank.controller.TransactionController;

import java.util.Scanner;

public class UserMenuUI {

    private final Scanner scanner;
    private final TransactionController transactionController;

    public UserMenuUI(Scanner scanner, TransactionController transactionController) {
        this.scanner = scanner;
        this.transactionController = transactionController;
    }

    public void show(String username) {

        while (true) {
            System.out.println("\n========== USER DASHBOARD ==========");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Balance");
            System.out.println("5. History");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            int choice = readChoice();

            try {
                switch (choice) {
                    case 1 -> transactionController.deposit(username);
                    case 2 -> transactionController.withdraw(username);
                    case 3 -> transactionController.transfer(username);
                    case 4 -> transactionController.balance(username);
                    case 5 -> transactionController.history(username);
                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }

            pause();
        }
    }

    private int readChoice() {
        while (!scanner.hasNextInt()) {
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
