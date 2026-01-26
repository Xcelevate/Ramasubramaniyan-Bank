package com.training.mybank.controller;

import com.training.mybank.entities.TransactionEntity;
import com.training.mybank.service.TransactionService;

import java.util.List;
import java.util.Scanner;

public class TransactionController {

    private final TransactionService transactionService;
    private final Scanner sc = new Scanner(System.in);

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void deposit(String username) {

        System.out.print("Enter deposit amount: ");
        double amount = sc.nextDouble();

        transactionService.deposit(username, amount);

        System.out.println("\n✅ Deposit Successful");
        System.out.printf("Amount Deposited : %.2f%n", amount);
    }


    public void withdraw(String username) {

        System.out.print("Enter withdrawal amount: ");
        double amount = sc.nextDouble();

        transactionService.withdraw(username, amount);

        System.out.println("\n✅ Withdrawal Successful");
        System.out.printf("Amount Withdrawn : %.2f%n", amount);
    }


    public void transfer(String username) {

        System.out.print("Enter receiver username: ");
        String toUser = sc.nextLine();

        System.out.print("Enter transfer amount: ");
        double amount = sc.nextDouble();

        transactionService.transfer(username, toUser, amount);

        System.out.println("\n✅ Transfer Successful");
        System.out.printf("Transferred %.2f to %s%n", amount, toUser);
    }


    public void balance(String username) {

        double balance = transactionService.checkBalance(username);

        System.out.println("\n========== ACCOUNT BALANCE ==========");
        System.out.printf("Available Balance : %.2f%n", balance);
        System.out.println("====================================");
    }


    public void history(String username) {

        List<TransactionEntity> transactions =
                transactionService.getTransactionHistory(username);

        if (transactions == null || transactions.isEmpty()) {
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

}
