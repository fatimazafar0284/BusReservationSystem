package com.busreservation.model;

// Interface for CRUD abstraction
public interface Manageable {
    void add(Object obj);
    void delete(String id);
    void update(String id, Object obj);
    Object search(String id);
}