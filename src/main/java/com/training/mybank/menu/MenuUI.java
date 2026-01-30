package com.training.mybank.menu;

import com.training.mybank.config.JPAConfig;
import com.training.mybank.controller.*;
import com.training.mybank.entities.Role;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.repositories.*;
import com.training.mybank.service.*;

import javax.persistence.EntityManagerFactory;
import java.util.Scanner;

public class MenuUI {

    private final Scanner scanner = new Scanner(System.in);
    private EntityManagerFactory emf;

    private AuthController authController;
    private RegistrationController registrationController;
    private TransactionController transactionController;
    private ForgotPasswordController forgotPasswordController;
    private AdminController adminController;

    /* ================= ENTRY ================= */

    public void start() {
        initialize();
        mainMenu();
        shutdown();
    }

    /* ================= INIT ================= */

    private void initialize() {

        emf = JPAConfig.getEntityManagerFactory();

        UserRepository userRepository = new UserRepository();
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        authController = new AuthController(
                new AuthService(emf, userRepository)
        );

        registrationController = new RegistrationController(
                new RegistrationService(emf, userRepository, accountRepository)
        );

        forgotPasswordController = new ForgotPasswordController(
                new ForgotPasswordService(emf, userRepository, accountRepository)
        );

        transactionController = new TransactionController(
                new TransactionService(emf, accountRepository, transactionRepository)
        );

        AdminService adminService = new AdminService(
                emf,
                userRepository,
                accountRepository,
                transactionRepository
        );

        adminController = new AdminController(adminService);
    }

    /* ================= MAIN MENU ================= */

    private void mainMenu() {

        while (true) {
            System.out.println("\n========== MY BANK ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = readChoice();

            switch (choice) {

                case 1 -> {
                    registrationController.register();
                    pause();
                }

                case 2 -> {
                    try {
                        UserEntity user = authController.login();
                        System.out.println("✅ Login successful");

                        if (Role.ADMIN.equals(user.getRole())) {
                            adminMenu(user);
                        } else {
                            userMenu(user.getUsername());
                        }

                    } catch (Exception e) {
                        System.out.println("❌ Login failed: " + e.getMessage());
                        System.out.print("Forgot password? (Y/N): ");

                        String opt = scanner.nextLine();
                        if (opt.equalsIgnoreCase("Y")) {
                            forgotPasswordController.forgotPassword();
                        }
                    }
                }

                case 3 -> {
                    System.out.println("Thank you for using MyBank.");
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ================= ADMIN MENU ================= */

    private void adminMenu(UserEntity admin) {

        while (true) {
            System.out.println("\n================================");
            System.out.println("        ADMIN CONTROL PANEL      ");
            System.out.println("================================");
            System.out.println("Logged in as : " + admin.getUsername());
            System.out.println("--------------------------------");
            System.out.println("1. Delete User (Soft Delete)");
            System.out.println("2. List All Users");
            System.out.println("3. View User Transactions");
            System.out.println("4. Add New User");
            System.out.println("5. Freeze Account");
            System.out.println("6. Unfreeze Account");
            System.out.println("7. List All Accounts");
            System.out.println("8. Logout");
            System.out.print("Choose option: ");

            int choice = readChoice();

            try {
                switch (choice) {
                    case 1 -> adminController.deleteUser(admin.getUsername());
                    case 2 -> adminController.listUsers();
                    case 3 -> adminController.viewUserTransactions();
                    case 4 -> adminController.addUser();
                    case 5 -> adminController.freezeAccount();
                    case 6 -> adminController.unfreezeAccount();
                    case 7 -> adminController.listAllAccounts();
                    case 8 -> {
                        System.out.println("Logging out admin...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }

            pause();
        }
    }

    /* ================= USER MENU ================= */

    private void userMenu(String username) {

        while (true) {
            System.out.println("\n================================");
            System.out.println("            USER DASHBOARD       ");
            System.out.println("================================");
            System.out.println("Logged in as : " + username);
            System.out.println("--------------------------------");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            int choice = readChoice();

            try {
                switch (choice) {
                    case 1 -> transactionController.deposit(username);
                    case 2 -> transactionController.withdraw(username);
                    case 3 -> transactionController.transfer(username);
                    case 4 -> transactionController.balance(username);
                    case 5 -> transactionController.history(username);
                    case 6 -> {
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ Operation failed: " + e.getMessage());
            }

            pause();
        }
    }

    /* ================= HELPERS ================= */

    private int readChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /* ================= SHUTDOWN ================= */

    private void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        scanner.close();
    }
}
