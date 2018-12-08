package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "12")
public class TicketStatus extends BaseEntity {

    @ValueField(ID = "37")
    private String name;
}
