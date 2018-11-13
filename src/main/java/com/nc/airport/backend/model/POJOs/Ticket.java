package com.nc.airport.backend.model.POJOs;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.List;
import com.nc.airport.backend.eav.annotations.attribute.value.Reference;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;
import com.nc.airport.backend.model.POJOs.Users.AuthorizedUser;

@ObjectType(ID = "")
public class Ticket {

    @Attribute(ID = "")
    @Value
    private int ticketId;

    @Attribute(ID = "")
    @Reference
    private Seat seat;

    @Attribute(ID = "")
    @Reference
    private Flight flight;

    @Attribute(ID = "")
    @List
    private PassengerType passengerType;

    @Attribute(ID = "")
    @Reference
    private AuthorizedUser client;



    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }



    enum PassengerType {
        Adult,
        Child,
        Baby
    }
}
