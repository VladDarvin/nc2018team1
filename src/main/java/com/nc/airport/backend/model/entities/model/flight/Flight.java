package com.nc.airport.backend.model.entities.model.flight;


import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ObjectType(ID = "3")
public class Flight {

    @DateField(ID = "6")
    private LocalDateTime departureDatetime;

    @DateField(ID = "7")
    private LocalDateTime arrivalDatetime;

    @ReferenceField(ID = "8")
    private int airplaneId;

    @ValueField(ID = "9")
    private double baseCost;

    @ReferenceField(ID = "10")
    private int arrivalAirport;

    @ReferenceField(ID = "11")
    private int departureAirport;

//    public LocalDateTime getDepartureDatetime() {
//        return departureDatetime;
//    }
//
//    public void setDepartureDatetime(LocalDateTime departureDatetime) {
//        this.departureDatetime = departureDatetime;
//    }
//
//    public LocalDateTime getArrivalDatetime() {
//        return arrivalDatetime;
//    }
//
//    public void setArrivalDatetime(LocalDateTime arrivalDatetime) {
//        this.arrivalDatetime = arrivalDatetime;
//    }
//
//    public int getAirplaneId() {
//        return airplaneId;
//    }
//
//    public void setAirplaneId(int airplaneId) {
//        this.airplaneId = airplaneId;
//    }
//
//    public double getBaseCost() {
//        return baseCost;
//    }
//
//    public void setBaseCost(double baseCost) {
//        this.baseCost = baseCost;
//    }
//
//    public int getArrivalAirport() {
//        return arrivalAirport;
//    }
//
//    public void setArrivalAirport(int arrivalAirport) {
//        this.arrivalAirport = arrivalAirport;
//    }
//
//    public int getDepartureAirport() {
//        return departureAirport;
//    }
//
//    public void setDepartureAirport(int departureAirport) {
//        this.departureAirport = departureAirport;
//    }
}
