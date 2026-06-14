package com.busreservation.model;

// Inheritance — Admin extends User
public class Admin extends User {

    private String adminCode;

    public Admin(String name, String email, String password) {
        super(name, email, password, "0300-0000000");
        this.adminCode = "ADMIN-001";
    }

    public Admin(String name, String email, String password, String adminCode) {
        super(name, email, password);
        this.adminCode = adminCode;
    }

    public String getAdminCode() { return adminCode; }

    // Polymorphism — Method Overriding
    @Override
    public String getRole() { return "Admin"; }

    @Override
    public String getDetails() {
        return super.getDetails() + "\nAdmin Code: " + adminCode;
    }

    @Override
    public String getSummary() {
        return "[ADMIN] " + getName() + " (" + getEmail() + ")";
    }
}