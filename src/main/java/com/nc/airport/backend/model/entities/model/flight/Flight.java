package com.nc.airport.backend.model.entities.model.flight;


import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@ObjectType(ID = "3")
@Getter
@Setter
@ToString(callSuper = true)
public class Flight extends BaseEntity {

    @ValueField(ID = "62")
    private BigInteger flightNumber;

    @DateField(ID = "64")
    private LocalDateTime expectedDepartureDatetime;

    @DateField(ID = "6")
    private LocalDateTime actualDepartureDatetime;

    @DateField(ID = "65")
    private LocalDateTime expectedArrivalDatetime;

    @DateField(ID = "7")
    private LocalDateTime actualArrivalDatetime;

    @ReferenceField(ID = "8")
    private BigInteger airplaneId;

    @ValueField(ID = "9")
    private BigDecimal baseCost;

    @ReferenceField(ID = "10")
    private BigInteger arrivalAirportId;

    @ReferenceField(ID = "11")
    private BigInteger departureAirportId;

    @ListField(ID = "63")
    private FlightStatus status;
}
