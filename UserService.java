package com.busreservation.service;

import com.busreservation.model.Admin;
import com.busreservation.model.Manageable;
import com.busreservation.model.User;

import java.util.ArrayList;
import java.util.List;

// Implements Manageable interface — CRUD + Search
public class UserService implements Manageable {

    private static final List<User> users = new ArrayList<>();

    // Singleton instance
    private static UserService instance;
    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    // ── CRUD Operations ──────────────────────────────

    // CREATE
    public static boolean register(User u) {
        for (User x : users) {
            if (x.getEmail().equalsIgnoreCase(u.getEmail())) return false;
        }
        users.add(u);
        return true;
    }

    public static void addUser(User u) { users.add(u); }

    // READ
    public static List<User> getAllUsers() { return users; }

    // LOGIN — Authentication
    public static User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // UPDATE
    public static boolean updateUser(String email, String newName,
                                     String newPhone, String newPassword) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                u.setName(newName);
                u.setPhone(newPhone);
                if (!newPassword.isEmpty()) u.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public static boolean deleteUser(String email) {
        return users.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }

    // SEARCH — Method Overloading
    public static User searchByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public static List<User> searchByName(String name) {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getName().toLowerCase().contains(name.toLowerCase())) result.add(u);
        }
        return result;
    }

    // Passengers only
    public static List<User> getPassengers() {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getRole().equals("Passenger")) result.add(u);
        }
        return result;
    }


    @Override public void add(Object obj)          { if (obj instanceof User) addUser((User) obj); }
    @Override public void delete(String id)        { deleteUser(id); }
    @Override public void update(String id, Object o) { /* handled via updateUser */ }
    @Override public Object search(String id)      { return searchByEmail(id); }
}