package com.nc.airport.backend.model.dto;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Country;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@ToString(callSuper = true)
public class AirportDto extends BaseEntity {
    private String name;
    private String address;
    private String city;
    private Country country;

    public AirportDto() {
    }

    public AirportDto(Airport airport, Country country) {
        super(airport);
        this.name = airport.getName();
        this.address = airport.getAddress();
        this.city = airport.getCity();
        this.country = country;
    }
}
