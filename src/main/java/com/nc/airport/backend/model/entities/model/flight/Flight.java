package com.nc.airport.backend.model.entities.model.flight;


import java.time.LocalDateTime;

public class Flight {

    private int id;
    private String flightCol;
    private LocalDateTime departureDatetime;
    private LocalDateTime arrivalDatetime;
    private int airplaneId;
    private double baseCost;
    private int arrivalAirport;
    private int departureAirport;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightCol() {
        return flightCol;
    }

    public void setFlightCol(String flightCol) {
        this.flightCol = flightCol;
    }

    public LocalDateTime getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(LocalDateTime departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public LocalDateTime getArrivalDatetime() {
        return arrivalDatetime;
    }

    public void setArrivalDatetime(LocalDateTime arrivalDatetime) {
        this.arrivalDatetime = arrivalDatetime;
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public int getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(int arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public int getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(int departureAirport) {
        this.departureAirport = departureAirport;
    }
}
