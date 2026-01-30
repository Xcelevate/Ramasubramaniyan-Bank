package com.training.mybank.controller;

import com.training.mybank.entities.*;
import com.training.mybank.service.AdminService;
import com.training.mybank.util.PasswordUtil;

import java.util.List;
import java.util.Scanner;

public class AdminController {

    private final AdminService adminService;
    private final Scanner sc = new Scanner(System.in);

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void deleteUser(String adminUsername) {
        System.out.print("Enter username to delete: ");
        adminService.deleteUser(adminUsername, sc.nextLine());
        System.out.println("User deleted successfully.");
    }

    public void listUsers() {

        List<UserEntity> users = adminService.listAllUsers();

        System.out.println("\n----------------------------------------------------");
        System.out.printf("%-15s %-25s %-10s %-10s%n",
                "USERNAME", "EMAIL", "ROLE", "ACTIVE");
        System.out.println("----------------------------------------------------");

        for (UserEntity u : users) {
            System.out.printf("%-15s %-25s %-10s %-10s%n",
                    u.getUsername(),
                    u.getEmail(),
                    u.getRole(),
                    u.getIsActive() ? "YES" : "NO");
        }

        System.out.println("----------------------------------------------------");
        System.out.println("Total users: " + users.size());
    }
    public void listAllAccounts() {

        List<AccountEntity> accounts = adminService.listAllAccounts();

        System.out.println("\n------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-10s%n",
                "ID", "ACCOUNT NUMBER", "FROZEN", "BALANCE");
        System.out.println("------------------------------------------");

        for (AccountEntity acc : accounts) {
            System.out.printf("%-10d %-20s %-10s %-10.2f%n",
                    acc.getId(),
                    acc.getAccountNumber(),
                    acc.getIsFrozen() ? "YES" : "NO",
                    acc.getBalance());
        }

        System.out.println("------------------------------------------");
        System.out.println("Total accounts: " + accounts.size());
    }


    public void viewUserTransactions() {

        System.out.print("Enter username to view transactions: ");
        String username = sc.nextLine();

        List<TransactionEntity> txs =
                adminService.getUserTransactions(username);

        if (txs.isEmpty()) {
            System.out.println("No transactions found for user.");
            return;
        }

        System.out.println("\n---------------- TRANSACTIONS ----------------");
        for (TransactionEntity tx : txs) {
            System.out.println(tx);
        }
        System.out.println("---------------------------------------------");
    }


    public void addUser() {

        UserEntity user = new UserEntity();

        System.out.println("\n---- ADD NEW USER ----");

        System.out.print("Username: ");
        user.setUsername(sc.nextLine());

        System.out.print("Email: ");
        user.setEmail(sc.nextLine());

        System.out.print("Full Name: ");
        user.setFullName(sc.nextLine());

        System.out.print("Password: ");
        user.setPassword(PasswordUtil.hash(sc.nextLine()));

        user.setRole(Role.USER);
        user.setIsActive(true);

        adminService.addUser(user);

        System.out.println("✅ User created successfully.");
    }


    public void freezeAccount() {

        System.out.print("Enter account ID: ");
        long accId = sc.nextLong();
        sc.nextLine(); // clear buffer

        adminService.toggleFreezeAccount(accId);

        System.out.println("✅ Account freeze status updated.");
    }

}
