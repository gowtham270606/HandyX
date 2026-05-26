package com.handyx.features.signup;

import com.handyx.data.dto.Employee;
import com.handyx.util.ConsoleInput;

import java.util.Scanner;

public class SignUpView {

    private final SignUpModel signUpModel;
    private final Scanner scanner;

    public SignUpView() {
        this.signUpModel = new SignUpModel(this);
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        System.out.println();
        System.out.println("Create a New HandyX Account");
        System.out.println("Select Role:");
        System.out.println("1. Manager");
        System.out.println("2. Employee");
        System.out.print("Choose: ");
        String roleChoice = scanner.nextLine().trim();

        Employee.Role role;
        if (roleChoice.equals("1")) {
            role = Employee.Role.MANAGER;
        } else if (roleChoice.equals("2")) {
            role = Employee.Role.EMPLOYEE;
        } else {
            System.out.println("Invalid");
            return;
        }

        System.out.print("Full Name     : ");
        String name = scanner.nextLine().trim();
        System.out.print("Username      : ");
        String username = scanner.nextLine().trim();
        System.out.print("Password      : ");
        String password = scanner.nextLine().trim();

        String managerUsername = null;
        if (role == Employee.Role.EMPLOYEE) {
            System.out.print("Manager Username: ");
            managerUsername = scanner.nextLine().trim();
        }

        signUpModel.signUp(name, username, password, role, managerUsername);
    }

    void onSignUpSuccessful(Employee employee) {
        System.out.println("Account created successfully!");
        System.out.println("Your Employee ID: " + employee.getEmployeeId());
        System.out.println("Sign to continue.");
    }

    void onSignUpFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
