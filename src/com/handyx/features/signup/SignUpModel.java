package com.handyx.features.signup;

import com.handyx.data.dto.Employee;
import com.handyx.data.repository.HandyXDB;

class SignUpModel {

    private final SignUpView signUpView;

    SignUpModel(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    void signUp(String name, String username, String password, Employee.Role role, String managerUsername) {
        if (name == null || name.isEmpty()) {
            signUpView.onSignUpFailed("Name cannot be empty");
            return;
        }
        if (username == null || username.isEmpty()) {
            signUpView.onSignUpFailed("Username cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()) {
            signUpView.onSignUpFailed("Password cannot be empty");
            return;
        }
        if (role == null) {
            signUpView.onSignUpFailed("Role must be selected");
            return;
        }

        if (HandyXDB.getInstance().getEmployeeByUsername(username) != null) {
            signUpView.onSignUpFailed("Username already exists");
            return;
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setRole(role);
        employee.setManagerUsername(managerUsername);

        Employee saved = HandyXDB.getInstance().addEmployee(employee);
        if (saved != null) {
            signUpView.onSignUpSuccessful(saved);
        } else {
            signUpView.onSignUpFailed("Failed to create account");
        }
    }
}
