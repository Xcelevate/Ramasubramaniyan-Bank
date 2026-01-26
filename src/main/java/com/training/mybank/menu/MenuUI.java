package com.training.mybank.menu;

import com.training.mybank.config.JPAConfig;
import com.training.mybank.controller.*;
import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.TransactionDAO;
import com.training.mybank.dao.UserDAO;
import com.training.mybank.exceptions.AuthenticationFailedException;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.service.AuthService;
import com.training.mybank.service.ForgotPasswordService;
import com.training.mybank.service.RegistrationService;
import com.training.mybank.service.TransactionService;

import javax.persistence.EntityManager;
import java.util.Scanner;

public class MenuUI {

    private final Scanner scanner = new Scanner(System.in);
    private EntityManager em;
    private AuthController authController;
    private RegistrationController registrationController;
    private TransactionController transactionController;
    private ForgotPasswordController forgotPasswordController;
    private AccountController accountController;

    /* ---------------- ENTRY POINT ---------------- */

    public void start() {
        initialize();
        mainMenu();
        shutdown();
    }

    /* ---------------- INITIALIZATION ---------------- */

    private void initialize() {

        em = JPAConfig.getEntityManager();

        // DAOs
        UserDAO userDAO = new UserDAO(em);
        AccountDAO accountDAO = new AccountDAO(em);
        TransactionDAO transactionDAO = new TransactionDAO(em);

        // Services
        AuthService authService = new AuthService(userDAO);
        RegistrationService registrationService =
                new RegistrationService(userDAO, accountDAO, em);
        ForgotPasswordService forgotPasswordService = new ForgotPasswordService(em, userDAO, accountDAO);
        TransactionService transactionService =
                new TransactionService(em, accountDAO, transactionDAO);

        // Controllers
        authController = new AuthController(authService);
        accountController = new AccountController(accountDAO);
        registrationController = new RegistrationController(registrationService);
        transactionController = new TransactionController(transactionService);
        forgotPasswordController = new ForgotPasswordController(forgotPasswordService);
        ;
    }

    /* ---------------- MAIN MENU ---------------- */

    private void mainMenu() {

        while (true) {
            System.out.println("\n========== MY BANK ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = readChoice();

            switch (choice) {
                case 1:
                    registrationController.register();
                    pause();
                    break;

                case 2:
                    try {
                        String user = authController.login();
                        System.out.println("Login successful");
                        userMenu(user);

                    } catch (AuthenticationFailedException e) {

                        System.out.println("Wrong credentials.");
                        System.out.print("Forgot password? (Y/N): ");
                        String forgot = scanner.nextLine();

                        if (forgot.equalsIgnoreCase("Y")) {
                            try {
                                forgotPasswordController.forgotPassword();
                            } catch (BankingException ex) {
                                System.out.println("Error: " + ex.getMessage());
                            }
                        }
                    } catch (BankingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;


                case 3:
                    System.out.println("Thank you for using MyBank.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /* ---------------- USER TRANSACTION MENU ---------------- */

    private void userMenu(String username) {

        while (true) {
            System.out.println("\n====== USER MENU ======");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. ListAccounts :");
            System.out.println("7. Logout");
            System.out.print("Choose: ");

            int choice = readChoice();

            try {
                switch (choice) {
                    case 1:
                        transactionController.deposit(username);
                        pause();
                        break;

                    case 2:
                        transactionController.withdraw(username);
                        pause();
                        break;

                    case 3:
                        transactionController.transfer(username);
                        pause();
                        break;

                    case 4:
                        transactionController.balance(username);
                        pause();
                        break;

                    case 5:
                        transactionController.history(username);
                        pause();
                        break;
                    case 6:
                        accountController.listAccountsSortedByAccountNumber();
                        pause();
                        break;
                    case 7:
                        System.out.println("Logged out successfully.");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause();
            }
        }
    }

    /* ---------------- HELPERS ---------------- */

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

    /* ---------------- SHUTDOWN ---------------- */

    private void shutdown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        scanner.close();
    }
}
