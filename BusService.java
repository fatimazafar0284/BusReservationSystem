package com.busreservation.service;

import com.busreservation.model.Bus;
import com.busreservation.model.Manageable;

import java.util.ArrayList;
import java.util.List;

public class BusService implements Manageable {

    private static final List<Bus> buses = new ArrayList<>();

    // ── CRUD ──────────────────────────────────────

    // CREATE
    public static void addBus(Bus b) { buses.add(b); }

    // READ
    public static List<Bus> getAllBuses() { return buses; }

    // UPDATE
    public static boolean updateBus(String busId, String name, String route,
                                    int seats, String dep, String arr,
                                    String date, double fare, String type) {
        for (Bus b : buses) {
            if (b.getBusId().equalsIgnoreCase(busId)) {
                b.setBusName(name);
                b.setRoute(route);
                b.setTotalSeats(seats);
                b.setDepartureTime(dep);
                b.setArrivalTime(arr);
                b.setTravelDate(date);
                b.setFare(fare);
                b.setBusType(type);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public static boolean deleteBus(String busId) {
        return buses.removeIf(b -> b.getBusId().equalsIgnoreCase(busId));
    }

    // SEARCH — Method Overloading
    public static Bus searchById(String busId) {
        for (Bus b : buses) {
            if (b.getBusId().equalsIgnoreCase(busId)) return b;
        }
        return null;
    }

    public static Bus searchByName(String name) {
        for (Bus b : buses) {
            if (b.getBusName().equalsIgnoreCase(name)) return b;
        }
        return null;
    }

    public static List<Bus> searchByRoute(String source, String destination) {
        List<Bus> result = new ArrayList<>();
        for (Bus b : buses) {
            String r = b.getRoute().toLowerCase();
            if (r.contains(source.toLowerCase()) && r.contains(destination.toLowerCase()))
                result.add(b);
        }
        return result;
    }

    public static List<Bus> searchByDate(String date) {
        List<Bus> result = new ArrayList<>();
        for (Bus b : buses) {
            if (b.getTravelDate().equals(date)) result.add(b);
        }
        return result;
    }

    public static List<Bus> searchByRouteAndDate(String source, String dest, String date) {
        List<Bus> result = new ArrayList<>();
        for (Bus b : buses) {
            String r = b.getRoute().toLowerCase();
            if (r.contains(source.toLowerCase())
                    && r.contains(dest.toLowerCase())
                    && b.getTravelDate().equals(date))
                result.add(b);
        }
        return result;
    }

    // ── Manageable Interface ──
    @Override public void add(Object obj)             { if (obj instanceof Bus) addBus((Bus) obj); }
    @Override public void delete(String id)           { deleteBus(id); }
    @Override public void update(String id, Object o) { /* handled above */ }
    @Override public Object search(String id)         { return searchById(id); }

    // ── Sample Data ───────────────────────────────
    public static void addSampleData() {
        buses.add(new Bus("BUS-001", "Daewoo Express",   "Lahore → Islamabad", 40, "08:00 AM", "02:00 PM", "2025-07-10", 1800, "AC"));
        buses.add(new Bus("BUS-002", "Faisal Movers",    "Karachi → Lahore",   36, "09:00 PM", "07:00 AM", "2025-07-10", 2200, "Sleeper"));
        buses.add(new Bus("BUS-003", "Bilal Travels",    "Islamabad → Peshawar",30,"07:00 AM", "12:00 PM", "2025-07-10", 1200, "Non-AC"));
        buses.add(new Bus("BUS-004", "Skyways",          "Multan → Lahore",    40, "06:00 AM", "10:00 AM", "2025-07-11", 900,  "AC"));
        buses.add(new Bus("BUS-005", "Niazi Express",    "Lahore → Karachi",   44, "08:00 PM", "09:00 AM", "2025-07-11", 2500, "Sleeper"));
        buses.add(new Bus("BUS-006", "Road King",        "Quetta → Karachi",   36, "10:00 AM", "08:00 PM", "2025-07-12", 1600, "AC"));
        buses.add(new Bus("BUS-007", "Kohistan Express", "Lahore → Multan",    40, "11:00 AM", "03:00 PM", "2025-07-12", 800,  "Non-AC"));
        buses.add(new Bus("BUS-008", "Daewoo Express",   "Islamabad → Lahore", 40, "03:00 PM", "09:00 PM", "2025-07-13", 1800, "AC"));
    }
}