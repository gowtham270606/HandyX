package com.handyx.features.home;

import com.handyx.data.dto.Employee;

class HomeModel {

    private final HomeView homeView;

    HomeModel(HomeView homeView) {
        this.homeView = homeView;
    }

    void init(Employee employee) {
        if (employee == null || employee.getRole() == null) {
            homeView.showUnauthorized();
            return;
        }
        switch (employee.getRole()) {
            case MANAGER:   homeView.showManagerMenu();  break;
            case EMPLOYEE:  homeView.showEmployeeMenu(); break;
            default:        homeView.showUnauthorized(); break;
        }
    }
}
