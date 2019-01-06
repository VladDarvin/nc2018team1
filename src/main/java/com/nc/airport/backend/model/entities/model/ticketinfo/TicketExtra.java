package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "11")
@Getter
@Setter
@ToString(callSuper = true)
public class TicketExtra extends BaseEntity {

    @ReferenceField(ID = "34")
    private BigInteger ticketId;

    @ReferenceField(ID = "35")
    private BigInteger extraId;

    @ReferenceField(ID = "36")
    private int quantity;
}
