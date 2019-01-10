package com.nc.airport.backend.model.dto;

import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PassengerPassportDTO {
    private Passenger passenger;
    private Passport passport;
}
