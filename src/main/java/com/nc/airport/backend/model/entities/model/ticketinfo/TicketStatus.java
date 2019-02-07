package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;

public enum TicketStatus {

    @ListValue(ID = "6")
    NEW("NEW"),

    @ListValue(ID = "7")
    BOUGHT("NEW"),

    @ListValue(ID = "8")
    ARCHIVED("NEW");


    private String statusName;

    TicketStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
