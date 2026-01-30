package com.training.mybank.menu;

import com.training.mybank.controller.AuthController;
import com.training.mybank.controller.ForgotPasswordController;
import com.training.mybank.controller.RegistrationController;
import com.training.mybank.entities.Role;
import com.training.mybank.entities.UserEntity;

import java.util.Scanner;

public class MainMenuUI {

    private final Scanner scanner;
    private final AuthController authController;
    private final RegistrationController registrationController;
    private final ForgotPasswordController forgotPasswordController;
    private final MenuUI menuUI;

    public MainMenuUI(
            Scanner scanner,
            AuthController authController,
            RegistrationController registrationController,
            ForgotPasswordController forgotPasswordController,
            MenuUI menuUI
    ) {
        this.scanner = scanner;
        this.authController = authController;
        this.registrationController = registrationController;
        this.forgotPasswordController = forgotPasswordController;
        this.menuUI = menuUI;
    }

    public void show() {

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

                case 2 -> loginFlow();

                case 3 -> {
                    System.out.println("Thank you for using MyBank.");
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void loginFlow() {
        try {
            UserEntity user = authController.login();
            System.out.println("✅ Login successful");

            if (user.getRole() == Role.ADMIN) {
                menuUI.openAdminMenu(user.getUsername());
            } else {
                menuUI.openUserMenu(user.getUsername());
            }

        } catch (Exception e) {
            System.out.println("❌ Login failed: " + e.getMessage());
            System.out.print("Forgot password? (Y/N): ");

            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                forgotPasswordController.forgotPassword();
            }
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
