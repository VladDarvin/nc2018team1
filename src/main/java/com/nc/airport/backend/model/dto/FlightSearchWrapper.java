package com.nc.airport.backend.model.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class FlightSearchWrapper {
    String departureCity;
    String destinationCity;
    LocalDateTime departureDate;
    LocalDateTime returnDate;
}
