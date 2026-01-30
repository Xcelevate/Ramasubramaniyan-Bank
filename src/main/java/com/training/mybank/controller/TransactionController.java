package com.training.mybank.controller;

import com.training.mybank.entities.TransactionEntity;
import com.training.mybank.exceptions.BankingException;
import com.training.mybank.service.TransactionService;

import java.util.List;
import java.util.Scanner;

public class TransactionController {

    private final TransactionService transactionService;
    private final Scanner sc = new Scanner(System.in);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /* ---------- DEPOSIT ---------- */

    public void deposit(String username) {
        try {
            System.out.print("Enter deposit amount: ");
            double amount = readDouble();

            transactionService.deposit(username, amount);

            System.out.println("\n✅ Deposit Successful");
            System.out.printf("Amount Deposited : %.2f%n", amount);

        } catch (BankingException e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
        }
    }

    /* ---------- WITHDRAW ---------- */

    public void withdraw(String username) {
        try {
            System.out.print("Enter withdrawal amount: ");
            double amount = readDouble();

            transactionService.withdraw(username, amount);

            System.out.println("\n✅ Withdrawal Successful");
            System.out.printf("Amount Withdrawn : %.2f%n", amount);

        } catch (BankingException e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
        }
    }

    /* ---------- TRANSFER ---------- */

    public void transfer(String username) {
        try {
            System.out.print("Enter receiver username: ");
            String toUser = sc.nextLine().trim();

            if (toUser.isEmpty()) {
                throw new BankingException("Receiver username cannot be empty");
            }

            System.out.print("Enter transfer amount: ");
            double amount = readDouble();

            transactionService.transfer(username, toUser, amount);

            System.out.println("\n✅ Transfer Successful");
            System.out.printf("Transferred %.2f to %s%n", amount, toUser);

        } catch (BankingException e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
        }
    }

    /* ---------- BALANCE ---------- */

    public void balance(String username) {
        double balance = transactionService.checkBalance(username);

        System.out.println("\n========== ACCOUNT BALANCE ==========");
        System.out.printf("Available Balance : %.2f%n", balance);
        System.out.println("====================================");
    }

    /* ---------- HISTORY ---------- */

    public void history(String username) {

        List<TransactionEntity> transactions =
                transactionService.getTransactionHistory(username);

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions available.");
            return;
        }

        System.out.println("\n========== TRANSACTION HISTORY ==========");
        System.out.printf("%-5s %-10s %-10s %-15s %-20s%n",
                "No", "Type", "Amount", "Balance", "Date");

        System.out.println("-------------------------------------------------------------");

        int count = 1;
        for (TransactionEntity tx : transactions) {
            System.out.printf("%-5d %-10s %-10.2f %-15.2f %-20s%n",
                    count++,
                    tx.getTransactionType(),
                    tx.getAmount(),
                    tx.getBalanceAfter(),
                    tx.getCreatedAt());
        }

        System.out.println("=========================================");
    }

    /* ---------- SAFE INPUT ---------- */

    private double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
}
