package com.handyx;

import com.handyx.data.dto.Employee;
import com.handyx.data.repository.HandyXDB;
import com.handyx.features.signin.SignInView;
import com.handyx.features.signup.SignUpView;
import com.handyx.util.ConsoleInput;

import java.util.Scanner;

public class Main {

    public static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        // seedDefaultManager();
        System.out.println("Welcome to HandyX");
        System.out.println("Work Management System v" + VERSION);
        showLandingMenu();
    }

    // private static void seedDefaultManager() {
    // if (!HandyXDB.getInstance().hasEmployees()) {
    // Employee manager = new Employee();
    // manager.setName(DEFAULT_MANAGER_NAME);
    // manager.setUsername(DEFAULT_MANAGER_USERNAME);
    // manager.setPassword(DEFAULT_MANAGER_PASSWORD);
    // manager.setRole(Employee.Role.MANAGER);
    // manager.setDepartment("Management");
    // HandyXDB.getInstance().addEmployee(manager);
    // }
    // }

    private static void showLandingMenu() {
        Scanner scanner = ConsoleInput.getScanner();
        while (true) {
            System.out.println();
            System.out.println("1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    new SignInView().init();
                    break;
                case "2":
                    new SignUpView().init();
                    break;
                case "3":
                    System.out.println("Tq");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
