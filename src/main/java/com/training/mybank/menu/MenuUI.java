package com.training.mybank.menu;

import java.util.Scanner;

public class MenuUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Initialization initialization = new Initialization();

    private MainMenuUI mainMenuUI;
    private AdminMenuUI adminMenuUI;
    private UserMenuUI userMenuUI;

    public void start() {

        initialization.initialize();

        mainMenuUI = new MainMenuUI(
                scanner,
                initialization.getAuthController(),
                initialization.getRegistrationController(),
                initialization.getForgotPasswordController(),
                this
        );

        adminMenuUI = new AdminMenuUI(
                scanner,
                initialization.getAdminController()
        );

        userMenuUI = new UserMenuUI(
                scanner,
                initialization.getTransactionController()
        );

        mainMenuUI.show();

        shutdown();
    }

    /* ===== ROUTING ===== */

    public void openAdminMenu(String username) {
        adminMenuUI.show(username);
    }

    public void openUserMenu(String username) {
        userMenuUI.show(username);
    }

    private void shutdown() {
        scanner.close();
    }
}
