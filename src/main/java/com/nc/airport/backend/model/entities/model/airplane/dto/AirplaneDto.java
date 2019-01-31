package com.nc.airport.backend.model.entities.model.airplane.dto;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AirplaneDto extends BaseEntity {
    private String model;
    private Airline airline;
}
