package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@ObjectType(ID = "13")
public class Passenger extends BaseEntity {

    @ValueField(ID = "38")
    private String firstName;

    @ValueField(ID = "39")
    private String lastName;

    @ReferenceField(ID = "40")
    private BigInteger passportId;
}
