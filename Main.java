package com.busreservation;

import com.busreservation.model.Admin;
import com.busreservation.service.BusService;
import com.busreservation.service.UserService;
import com.busreservation.ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        initializeApplicationData();
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }

    private static void initializeApplicationData() {

        BusService.addSampleData();
        System.out.println("Sample buses loaded: " + BusService.getAllBuses().size());

        Admin admin = new Admin("Admin", "admin@bus.com", "admin123");
        UserService.addUser(admin);
        System.out.println(" Admin account created");

        UserService.addUser(new com.busreservation.model.User(
                "Ahmed Khan", "ahmed@gmail.com", "pass123", "03001234567"));
        UserService.addUser(new com.busreservation.model.User(
                "Fatima ", "fatima@gmail.com", "pass456", "03009876543"));
        System.out.println(" Sample passengers created");


        System.out.println(" BUS TICKET RESERVATION SYSTEM INITIALIZED");
        System.out.println("System Status:");
        System.out.println("   • Total Users: " + UserService.getAllUsers().size());
        System.out.println("   • Total Buses: " + BusService.getAllBuses().size());
        System.out.println(" Login Credentials:");
        System.out.println("   ADMIN:    admin@bus.com / admin123");
        System.out.println("   PASSENGER: ahmed@gmail.com / pass123");

    }
}