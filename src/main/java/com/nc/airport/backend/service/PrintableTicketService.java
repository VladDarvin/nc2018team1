package com.nc.airport.backend.service;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConsistencyException;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.model.dto.PrintableTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class PrintableTicketService {
    private EavCrudRepository repository;

    @Autowired
    public PrintableTicketService(EavCrudRepository repository) {
        this.repository = repository;
    }

    public PrintableTicket getPrintableTicket(Ticket ticket) {
        PrintableTicket printableTicket = new PrintableTicket();
        Flight flight;
        Airport departureAirport;
        Country departureCountry;
        Airport arrivalAirport;
        Country arrivalCountry;
        Airplane airplane;
        Airline airline;
        Seat seat;
        SeatType seatType;
        Passenger passenger;

        flight = getEntityById(ticket.getFlightId(), Flight.class);
        departureAirport = getEntityById(flight.getDepartureAirportId(), Airport.class);
        departureCountry = getEntityById(departureAirport.getCountryId(), Country.class);
        arrivalAirport = getEntityById(flight.getArrivalAirportId(), Airport.class);
        arrivalCountry = getEntityById(arrivalAirport.getCountryId(), Country.class);
        airplane = getEntityById(flight.getAirplaneId(), Airplane.class);
        airline = getEntityById(airplane.getAirlineId(), Airline.class);
        seat = getEntityById(ticket.getSeatId(), Seat.class);
        seatType = getEntityById(seat.getSeatTypeId(), SeatType.class);
        passenger = getEntityById(ticket.getPassengerId(), Passenger.class);

        printableTicket.setPassengerFirstName(passenger.getFirstName());
        printableTicket.setPassengerLastName(passenger.getLastName());
        printableTicket.setCost(calculateCost(flight, seat, seatType));
        printableTicket.setSeatType(seatType.getName());
        printableTicket.setSeatColumn(seat.getCol());
        printableTicket.setSeatRow(seat.getRow());
        printableTicket.setFlightNumber(flight.getFlightNumber());
        printableTicket.setExpectedDepartureDatetime(flight.getExpectedDepartureDatetime());
        printableTicket.setDepartureAirportName(departureAirport.getName());
        printableTicket.setDepartureCountry(departureCountry.getName());
        printableTicket.setDepartureCity(departureAirport.getCity());
        printableTicket.setDepartureAirportAddress(departureAirport.getAddress());
        printableTicket.setExpectedArrivalDatetime(flight.getExpectedArrivalDatetime());
        printableTicket.setArrivalAirportName(arrivalAirport.getName());
        printableTicket.setArrivalCountry(arrivalCountry.getName());
        printableTicket.setArrivalCity(arrivalAirport.getCity());
        printableTicket.setArrivalAirportAddress(arrivalAirport.getAddress());
        printableTicket.setAirlineName(airline.getName());
        printableTicket.setAirlineEmail(airline.getEmail());
        printableTicket.setAirlinePhoneNumber(airline.getPhoneNumber());

        return printableTicket;
    }

    @SuppressWarnings("unchecked")
    private <T> T getEntityById(BigInteger objectId, Class<T> entityClass) {
        Optional<BaseEntity> entityWrapper = repository.findById(objectId, entityClass);
        return (T) entityWrapper.
                orElseThrow(() -> new DatabaseConsistencyException("Invalid database instance: Ticket missing "
                        + entityClass.getSimpleName()));
    }

    private double calculateCost(Flight flight, Seat seat, SeatType seatType) {
        return flight.getBaseCost().doubleValue() + seat.getModifier() + seatType.getModifier();
    }
}
