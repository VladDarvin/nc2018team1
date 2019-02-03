package com.nc.airport.backend.model.entities.model.airplane.dto;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString(callSuper = true)
public class AirplaneDto extends BaseEntity {
    private String model;
    private Airline airline;

    public AirplaneDto() {
    }

    public AirplaneDto(Airplane airplane) {
        super(airplane);
        this.model = airplane.getModel();
        this.airline = new Airline(airplane.getAirlineId());
    }

    public AirplaneDto(Airplane airplane, Airline airline) {
        this(airplane);
        this.airline = airline;
    }

    public AirplaneDto(BigInteger objectId) {
        super(objectId);
    }
}
