package com.nc.airport.backend.model.entities.model.flight;


import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDateTime;

@ObjectType(ID = "3")
@Getter
@Setter
@ToString(callSuper = true)
public class Flight extends BaseEntity {

    @DateField(ID = "6")
    private LocalDateTime departureDatetime;

    @DateField(ID = "7")
    private LocalDateTime arrivalDatetime;

    @ReferenceField(ID = "8")
    private BigInteger airplaneId;

    @ValueField(ID = "9")
    private double baseCost;

    @ReferenceField(ID = "10")
    private BigInteger arrivalAirportId;

    @ReferenceField(ID = "11")
    private BigInteger departureAirportId;
}
