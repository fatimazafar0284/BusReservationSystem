package com.busreservation.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

// Ticket entity — implements Printable (Abstraction)
public class Ticket implements Printable {

    private String ticketId;
    private String passengerName;
    private String passengerEmail;
    private String busId;
    private String busName;
    private String route;
    private int    seatNumber;
    private double fare;
    private String bookingDate;
    private String travelDate;
    private String status; // BOOKED / CANCELLED

    // Constructor
    public Ticket(String passengerName, String passengerEmail,
                  String busId, String busName, String route,
                  int seatNumber, double fare, String travelDate) {
        this.ticketId       = generateTicketId();
        this.passengerName  = passengerName;
        this.passengerEmail = passengerEmail;
        this.busId          = busId;
        this.busName        = busName;
        this.route          = route;
        this.seatNumber     = seatNumber;
        this.fare           = fare;
        this.travelDate     = travelDate;
        this.bookingDate    = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status         = "BOOKED";
    }

    // Generate unique ticket ID
    private String generateTicketId() {
        return "TKT-" + (1000 + new Random().nextInt(9000));
    }

    // Getters
    public String getTicketId()       { return ticketId; }
    public String getPassengerName()  { return passengerName; }
    public String getPassengerEmail() { return passengerEmail; }
    public String getBusId()          { return busId; }
    public String getBusName()        { return busName; }
    public String getRoute()          { return route; }
    public int    getSeatNumber()     { return seatNumber; }
    public double getFare()           { return fare; }
    public String getBookingDate()    { return bookingDate; }
    public String getTravelDate()     { return travelDate; }
    public String getStatus()         { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String getDetails() {
        return "═══════════════════════════\n"
                + "     BUS TICKET RECEIPT\n"
                + "═══════════════════════════\n"
                + "Ticket ID  : " + ticketId + "\n"
                + "Passenger  : " + passengerName + "\n"
                + "Email      : " + passengerEmail + "\n"
                + "Bus        : " + busName + "\n"
                + "Route      : " + route + "\n"
                + "Seat No    : " + seatNumber + "\n"
                + "Travel Date: " + travelDate + "\n"
                + "Booked On  : " + bookingDate + "\n"
                + "Fare       : Rs. " + fare + "\n"
                + "Status     : " + status + "\n"
                + "═══════════════════════════";
    }

    @Override
    public String getSummary() {
        return ticketId + " | " + passengerName + " | " + busName + " | Seat " + seatNumber + " | " + status;
    }
}