package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airplane.Extra;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Map;

@ObjectType(ID = "10")
@Getter
@Setter
@ToString(callSuper = true)
public class Ticket extends BaseEntity {

    @ReferenceField(ID = "30")
    private BigInteger flightId;

    @ReferenceField(ID = "31")
    private BigInteger seatId;

    @ReferenceField(ID = "32")
    private BigInteger passengerId;

    @ReferenceField(ID = "33")
    private BigInteger ticketStatusId;

    @ListField(ID = "11")
    private Map<Extra, BigInteger> TicketHasExtra;


}
