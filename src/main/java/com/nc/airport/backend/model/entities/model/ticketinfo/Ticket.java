package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "10")
public class Ticket extends BaseEntity {

    @ReferenceField(ID = "30")
    private int flightId;

    @ReferenceField(ID = "31")
    private int seatId;

    @ReferenceField(ID = "32")
    private int passengerId;

    @ReferenceField(ID = "33")
    private int ticketStatusId;

//    public int getFlightId() {
//        return flightId;
//    }
//
//    public void setFlightId(int flightId) {
//        this.flightId = flightId;
//    }
//
//    public int getSeatId() {
//        return seatId;
//    }
//
//    public void setSeatId(int seatId) {
//        this.seatId = seatId;
//    }
//
//    public int getPassengerId() {
//        return passengerId;
//    }
//
//    public void setPassengerId(int passengerId) {
//        this.passengerId = passengerId;
//    }
//
//    public int getTicketStatusId() {
//        return ticketStatusId;
//    }
//
//    public void setTicketStatusId(int ticketStatusId) {
//        this.ticketStatusId = ticketStatusId;
//    }
}
