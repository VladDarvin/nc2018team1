package com.nc.airport.backend.model.POJOs;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Date;
import com.nc.airport.backend.eav.annotations.attribute.value.Reference;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@ObjectType(ID = "")
public class Flight {

    @Attribute(ID = "")
    @Value
    private String flightID;

    @Attribute(ID = "")
    @Value
    private String departure;

    @Attribute(ID = "")
    @Value
    private String destination;

    @Attribute(ID = "")
    @Date
    private LocalDate departureDate;

    @Attribute(ID = "")
    @Value
    private LocalTime departureTime;

    @Attribute(ID = "")
    @Date
    private LocalDate destinationDate;

    @Attribute(ID = "")
    @Value
    private LocalTime destinationTime;

    @Attribute(ID = "")
    @Reference
    private Plane plane;



    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDate getDestinationDate() {
        return destinationDate;
    }

    public void setDestinationDate(LocalDate destinationDate) {
        this.destinationDate = destinationDate;
    }

    public LocalTime getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(LocalTime destinationTime) {
        this.destinationTime = destinationTime;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
