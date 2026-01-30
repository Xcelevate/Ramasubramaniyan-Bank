package com.training.mybank.menu;

import com.training.mybank.config.JPAConfig;
import com.training.mybank.controller.*;
import com.training.mybank.repositories.AccountRepository;
import com.training.mybank.repositories.TransactionRepository;
import com.training.mybank.repositories.UserRepository;
import com.training.mybank.service.*;

import javax.persistence.EntityManagerFactory;

public class Initialization {

    private EntityManagerFactory emf;

    private AuthController authController;
    private RegistrationController registrationController;
    private TransactionController transactionController;
    private ForgotPasswordController forgotPasswordController;
    private AdminController adminController;

    public void initialize() {

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

        adminController = new AdminController(
                new AdminService(emf, userRepository, accountRepository, transactionRepository)
        );
    }

    /* ===== GETTERS ===== */

    public AuthController getAuthController() {
        return authController;
    }

    public RegistrationController getRegistrationController() {
        return registrationController;
    }

    public TransactionController getTransactionController() {
        return transactionController;
    }

    public ForgotPasswordController getForgotPasswordController() {
        return forgotPasswordController;
    }

    public AdminController getAdminController() {
        return adminController;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
