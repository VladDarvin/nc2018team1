package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;

public enum FlightStatus {

    @ListValue(ID = "9")
    SCHEDULED("Scheduled"),

    @ListValue(ID = "10")
    CHECK_IN("Check in"),

    @ListValue(ID = "11")
    GATE_OPEN("Gate open"),

    @ListValue(ID = "12")
    GATE_CLOSING("Gate closing"),

    @ListValue(ID = "13")
    GATE_CLOSED("Gate closed"),

    @ListValue(ID = "14")
    BOARDING("Boarding"),

    @ListValue(ID = "15")
    DEPARTED("Departed"),

    @ListValue(ID = "16")
    EXPECTING("Expecting"),

    @ListValue(ID = "17")
    LANDED("Landed"),

    @ListValue(ID = "18")
    DELAYED("Delayed"),

    @ListValue(ID = "19")
    CANCELED("Canceled");


    private String statusName;

    FlightStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

}