package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;

public enum FlightStatus {

    @ListValue(ID = "9")
    SCHEDULED("Scheduled"),

    @ListValue(ID = "10")
    CHECK_IN("Check in"),

    @ListValue(ID = "11")
    BOARDING("Boarding"),

    @ListValue(ID = "12")
    DEPARTED("Departed"),

    @ListValue(ID = "13")
    EXPECTING("Expected"),

    @ListValue(ID = "14")
    LANDED("Landed"),

    @ListValue(ID = "15")
    DELAYED("Delayed"),

    @ListValue(ID = "16")
    CANCELED("Canceled"),

    @ListValue(ID = "17")
    REDIRECTED("Redirected");


    private String statusName;

    FlightStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

}