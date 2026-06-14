package com.busreservation.model;

// Bus entity — Encapsulation
public class Bus implements Printable {

    private String busId;
    private String busName;
    private String route;
    private int    totalSeats;
    private String departureTime;
    private String arrivalTime;
    private String travelDate;
    private double fare;
    private String busType; // AC / Non-AC / Sleeper

    // Constructor Overloading
    public Bus(String busId, String busName, String route, int totalSeats) {
        this.busId         = busId;
        this.busName       = busName;
        this.route         = route;
        this.totalSeats    = totalSeats;
        this.departureTime = "08:00 AM";
        this.arrivalTime   = "02:00 PM";
        this.travelDate    = "2025-01-01";
        this.fare          = 1500.0;
        this.busType       = "AC";
    }

    public Bus(String busId, String busName, String route, int totalSeats,
               String departureTime, String arrivalTime, String travelDate,
               double fare, String busType) {
        this.busId         = busId;
        this.busName       = busName;
        this.route         = route;
        this.totalSeats    = totalSeats;
        this.departureTime = departureTime;
        this.arrivalTime   = arrivalTime;
        this.travelDate    = travelDate;
        this.fare          = fare;
        this.busType       = busType;
    }

    // Getters
    public String getBusId()        { return busId; }
    public String getBusName()      { return busName; }
    public String getRoute()        { return route; }
    public int    getTotalSeats()   { return totalSeats; }
    public String getDepartureTime(){ return departureTime; }
    public String getArrivalTime()  { return arrivalTime; }
    public String getTravelDate()   { return travelDate; }
    public double getFare()         { return fare; }
    public String getBusType()      { return busType; }

    // Setters
    public void setBusName(String n)       { this.busName       = n; }
    public void setRoute(String r)         { this.route         = r; }
    public void setTotalSeats(int s)       { this.totalSeats    = s; }
    public void setDepartureTime(String t) { this.departureTime = t; }
    public void setArrivalTime(String t)   { this.arrivalTime   = t; }
    public void setTravelDate(String d)    { this.travelDate    = d; }
    public void setFare(double f)          { this.fare          = f; }
    public void setBusType(String t)       { this.busType       = t; }

    @Override
    public String getDetails() {
        return "Bus ID: " + busId
                + "\nBus: " + busName
                + "\nRoute: " + route
                + "\nDate: " + travelDate
                + "\nDeparture: " + departureTime
                + "\nArrival: " + arrivalTime
                + "\nType: " + busType
                + "\nFare: Rs. " + fare
                + "\nTotal Seats: " + totalSeats;
    }

    @Override
    public String getSummary() {
        return busName + " | " + route + " | " + travelDate + " | Rs. " + fare;
    }
}