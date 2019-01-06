package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;

public enum  TicketStatus{
    @ListValue(ID = "7")
    NEW,
    @ListValue(ID = "8")
    BOUGHT,
    @ListValue(ID = "9")
    ARCHIVED
}
