package com.busreservation.model;


public class Route implements Printable {

    private String routeId;
    private String source;
    private String destination;
    private double distanceKm;
    private double baseFare;

    public Route(String routeId, String source, String destination, double distanceKm, double baseFare) {
        this.routeId     = routeId;
        this.source      = source;
        this.destination = destination;
        this.distanceKm  = distanceKm;
        this.baseFare    = baseFare;
    }


    public String getRouteId()      { return routeId; }
    public String getSource()       { return source; }
    public String getDestination()  { return destination; }
    public double getDistanceKm()   { return distanceKm; }
    public double getBaseFare()     { return baseFare; }


    public void setSource(String source)           { this.source      = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDistanceKm(double d)            { this.distanceKm  = d; }
    public void setBaseFare(double f)              { this.baseFare    = f; }

    public String getRouteDisplay() {
        return source + " → " + destination;
    }

    @Override
    public String getDetails() {
        return "Route ID: " + routeId
                + "\nFrom: " + source
                + "\nTo: " + destination
                + "\nDistance: " + distanceKm + " km"
                + "\nBase Fare: Rs. " + baseFare;
    }

    @Override
    public String getSummary() {
        return routeId + ": " + source + " → " + destination + " (Rs. " + baseFare + ")";
    }
}