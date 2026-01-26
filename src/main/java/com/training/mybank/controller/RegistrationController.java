package com.training.mybank.controller;

import com.training.mybank.service.RegistrationService;

import java.util.Scanner;

public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username :");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Full Name: ");
        String fullName=scanner.nextLine();

        System.out.println("Email: ");
        String email=scanner.nextLine();
        String accountNumber = registrationService.register(username,password,fullName,email);

        if(accountNumber!=null){
            System.out.println("Registration Successful");
            System.out.println("Your account Number: "+accountNumber);
        }
        else{
            System.out.println("User already exists");
        }
    }
}
