package com.nc.airport.backend.util.print.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrintableTicket {
    private String passengerFirstName;
    private String passengerLastName;
    private int cost;
    private String seatType;
    private int seatColumn;
    private int seatRow;
    private BigInteger flightNumber;
    private LocalDateTime expectedDepartureDatetime;
    private String departureAirportName;
    private String departureCountry;
    private String departureCity;
    private String departureAirportAddress;
    private LocalDateTime expectedArrivalDatetime;
    private String arrivalAirportName;
    private String arrivalCountry;
    private String arrivalCity;
    private String arrivalAirportAddress;
    private String airlineName;
    private String airlinePhoneNumber;
    private String airlineEmail;
}
