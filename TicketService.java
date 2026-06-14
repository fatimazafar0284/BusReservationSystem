package com.busreservation.service;

import com.busreservation.model.Manageable;
import com.busreservation.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketService implements Manageable {

    private static final List<Ticket> tickets = new ArrayList<>();

    // CREATE
    public static void addTicket(Ticket t) { tickets.add(t); }

    // READ
    public static List<Ticket> getAllTickets() { return tickets; }

    // UPDATE — Cancel ticket
    public static boolean cancelTicket(String ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId().equalsIgnoreCase(ticketId)
                    && t.getStatus().equals("BOOKED")) {
                t.setStatus("CANCELLED");
                return true;
            }
        }
        return false;
    }

    // DELETE permanently
    public static boolean deleteTicket(String ticketId) {
        return tickets.removeIf(t -> t.getTicketId().equalsIgnoreCase(ticketId));
    }

    // SEARCH — Method Overloading
    public static Ticket searchById(String ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId().equalsIgnoreCase(ticketId)) return t;
        }
        return null;
    }

    public static List<Ticket> searchByEmail(String email) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPassengerEmail().equalsIgnoreCase(email)) result.add(t);
        }
        return result;
    }

    public static List<Ticket> searchByBusId(String busId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getBusId().equalsIgnoreCase(busId)) result.add(t);
        }
        return result;
    }

    // Check if seat is booked on a bus
    public static boolean isSeatBooked(String busId, int seat) {
        for (Ticket t : tickets) {
            if (t.getBusId().equalsIgnoreCase(busId)
                    && t.getSeatNumber() == seat
                    && t.getStatus().equals("BOOKED"))
                return true;
        }
        return false;
    }

    // Revenue report
    public static double getTotalRevenue() {
        double total = 0;
        for (Ticket t : tickets) {
            if (t.getStatus().equals("BOOKED")) total += t.getFare();
        }
        return total;
    }

    public static int getTotalBooked() {
        int count = 0;
        for (Ticket t : tickets) if (t.getStatus().equals("BOOKED")) count++;
        return count;
    }

    public static int getTotalCancelled() {
        int count = 0;
        for (Ticket t : tickets) if (t.getStatus().equals("CANCELLED")) count++;
        return count;
    }

    // ── Manageable Interface ──
    @Override public void add(Object obj)             { if (obj instanceof Ticket) addTicket((Ticket) obj); }
    @Override public void delete(String id)           { deleteTicket(id); }
    @Override public void update(String id, Object o) { cancelTicket(id); }
    @Override public Object search(String id)         { return searchById(id); }
}