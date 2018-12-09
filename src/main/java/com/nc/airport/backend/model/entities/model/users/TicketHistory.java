package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "18")
@Getter
@Setter
@ToString(callSuper = true)
public class TicketHistory extends BaseEntity {

    @ReferenceField(ID = "57")
    private BigInteger ticketId;

    @ReferenceField(ID = "58")
    private BigInteger authorizedUserId;
}
