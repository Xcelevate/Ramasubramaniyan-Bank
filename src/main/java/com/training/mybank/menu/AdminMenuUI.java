package com.training.mybank.menu;

import com.training.mybank.controller.AdminController;

import java.util.Scanner;

public class AdminMenuUI {

    private final Scanner scanner;
    private final AdminController adminController;

    public AdminMenuUI(Scanner scanner, AdminController adminController) {
        this.scanner = scanner;
        this.adminController = adminController;
    }

    public void show(String adminUsername) {

        while (true) {
            System.out.println("\n========== ADMIN PANEL ==========");
            System.out.println("1. Delete User");
            System.out.println("2. List Users");
            System.out.println("3. View User Transactions");
            System.out.println("4. Add User");
            System.out.println("5. Freeze Account");
            System.out.println("6. Unfreeze Account");
            System.out.println("7. List Accounts");
            System.out.println("8. Logout");
            System.out.print("Choose: ");

            int choice = readChoice();

            try {
                switch (choice) {
                    case 1 -> adminController.deleteUser(adminUsername);
                    case 2 -> adminController.listUsers();
                    case 3 -> adminController.viewUserTransactions();
                    case 4 -> adminController.addUser();
                    case 5 -> adminController.freezeAccount();
                    case 6 -> adminController.unfreezeAccount();
                    case 7 -> adminController.listAllAccounts();
                    case 8 -> { return; }
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
