package com.busreservation.model;

// Base class — Encapsulation + Inheritance root
public class User implements Printable {

    // Private fields — Encapsulation
    private String name;
    private String email;
    private String password;
    private String phone;

    // Constructor Overloading
    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.phone    = "N/A";
    }

    public User(String name, String email, String password, String phone) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.phone    = phone;
    }

    // Getters & Setters — Encapsulation
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getPhone()    { return phone; }

    public void setName(String name)         { this.name     = name; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone)       { this.phone    = phone; }

    // Polymorphism — overridden in subclasses
    public String getRole() { return "Passenger"; }

    // Abstraction via Printable interface
    @Override
    public String getDetails() {
        return "Name: " + name + "\nEmail: " + email + "\nRole: " + getRole() + "\nPhone: " + phone;
    }

    @Override
    public String getSummary() {
        return name + " (" + email + ")";
    }
}