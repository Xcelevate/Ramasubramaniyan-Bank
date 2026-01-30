package com.training.mybank.controller;

import com.training.mybank.service.AuthService;

import java.util.Scanner;

public class AuthController {

    private final AuthService authService;

    private final Scanner sc = new Scanner(System.in);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public String login() {

        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        return authService.login(username, password);
    }
}
