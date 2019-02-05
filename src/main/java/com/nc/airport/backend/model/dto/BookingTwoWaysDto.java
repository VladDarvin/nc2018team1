package com.nc.airport.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingTwoWaysDto {
    private List<FlightDTO> departureFlights;
    private List<FlightDTO> returnFlights;
}
