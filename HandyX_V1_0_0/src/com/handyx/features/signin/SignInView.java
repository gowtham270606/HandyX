package com.handyx.features.signin;

import com.handyx.data.dto.Employee;
import com.handyx.features.home.HomeView;
import com.handyx.util.ConsoleInput;

import java.util.Scanner;

public class SignInView {

    private final SignInModel signInModel;
    private final Scanner scanner;
    private boolean authenticated;

    public SignInView() {
        this.signInModel = new SignInModel(this);
        this.scanner = ConsoleInput.getScanner();
        this.authenticated = false;
    }

    public void init() {
        System.out.println();
        System.out.println("        Sign In to HandyX");
        while (!authenticated) {
            System.out.print("Username : ");
            String username = scanner.nextLine();
            System.out.print("Password : ");
            String password = scanner.nextLine();
            signInModel.authenticate(
                    username == null ? null : username.trim(),
                    password);
            if (!authenticated) {
                System.out.println("1. Retry   2. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("2")) return;
            }
        }
    }

    void onSignInSuccessful(Employee employee) {
        authenticated = true;
        System.out.println("Welcome, " + employee.getName()
                + "  [" + employee.getRole() + "]");
        new HomeView(employee).init();
    }

    void onSignInFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
