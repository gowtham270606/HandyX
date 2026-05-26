package com.handyx.features.signin;

import com.handyx.data.dto.Employee;
import com.handyx.data.repository.HandyXDB;

class SignInModel {

    private final SignInView signInView;

    SignInModel(SignInView signInView) {
        this.signInView = signInView;
    }

    String validateUsername(String username) {
        if (username == null || username.trim().isEmpty())
            return "Username cannot be empty";
        return null;
    }

    String validatePassword(String password) {
        if (password == null || password.isEmpty())
            return "Password cannot be empty";
        return null;
    }

    void authenticate(String username, String password) {
        String usernameError = validateUsername(username);
        if (usernameError != null) {
            signInView.onSignInFailed(usernameError);
            return;
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            signInView.onSignInFailed(passwordError);
            return;
        }

        Employee employee = HandyXDB.getInstance().authenticate(username, password);
        if (employee == null) {
            signInView.onSignInFailed("Invalid");
            return;
        }
        signInView.onSignInSuccessful(employee);
    }
}
