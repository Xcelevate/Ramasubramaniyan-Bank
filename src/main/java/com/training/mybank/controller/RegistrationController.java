package com.training.mybank.controller;

import com.training.mybank.exceptions.BankingException;
import com.training.mybank.service.RegistrationService;

import java.util.Scanner;

public class RegistrationController {

    private final RegistrationService registrationService;
    private final Scanner scanner = new Scanner(System.in);

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void register() {
        try {
            System.out.print("Username : ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Full Name: ");
            String fullName = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            String accountNumber =
                    registrationService.register(username, password, fullName, email);

            System.out.println("✅ Registration successful");
            System.out.println("Your Account Number: " + accountNumber);

        } catch (BankingException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
        }
    }
}
