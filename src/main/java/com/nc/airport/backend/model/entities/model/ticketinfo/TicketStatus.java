package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;

@ObjectType(ID = "12")
public class TicketStatus {

    @ValueField(ID = "38")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
