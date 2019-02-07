package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import lombok.*;

import java.math.BigInteger;

@ObjectType(ID = "10")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends BaseEntity {

    @ReferenceField(ID = "30")
    private BigInteger flightId;

    @ReferenceField(ID = "31")
    private BigInteger seatId;

    @ReferenceField(ID = "32")
    private BigInteger passengerId;

    @ListField(ID = "33")
    private TicketStatus ticketStatus;

//   ?

//    @ListField(ID = "11")
//    private BigInteger ticketExtra;
}
