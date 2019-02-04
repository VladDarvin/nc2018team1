package com.nc.airport.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrintableTicket {
    private String passengerFirstName;
    private String passengerLastName;
    private String seatType;
    private int seatColumn;
    private int seatRow;
    private String flightNumber;
    private LocalDateTime expectedDepartureDatetime;
    private String departureCity;
    private LocalDateTime expectedArrivalDatetime;
    private String arrivalCity;
    private String airlineName;
    private String airlinePhoneNumber;
    private String airlineEmail;
}
