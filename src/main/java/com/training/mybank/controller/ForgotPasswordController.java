package com.training.mybank.controller;

import com.training.mybank.exceptions.InvalidRecoveryDetailsException;
import com.training.mybank.service.ForgotPasswordService;

import java.util.Scanner;

public class ForgotPasswordController {

    private final ForgotPasswordService service;
    private final Scanner sc = new Scanner(System.in);

    public ForgotPasswordController(ForgotPasswordService service) {
        this.service = service;
    }

    public void forgotPassword() {

        System.out.println("\n====== PASSWORD RECOVERY ======");

        try {
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Registered Email: ");
            String email = sc.nextLine();

            System.out.print("Account Number: ");
            String accountNumber = sc.nextLine();

            System.out.print("New Password: ");
            String newPassword = sc.nextLine();

            System.out.print("Confirm Password: ");
            String confirmPassword = sc.nextLine();

            service.resetPassword(
                    username,
                    email,
                    accountNumber,
                    newPassword,
                    confirmPassword
            );

            System.out.println("✅ Password reset successful. Please login again.");

        } catch (InvalidRecoveryDetailsException e) {
            System.out.println("❌ Password reset failed: " + e.getMessage());
        }
    }

}
