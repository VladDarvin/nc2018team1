package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.model.entities.model.users.TicketHistory;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class FlightService extends AbstractService {

    private PassportService passportService;

    public FlightService(EavCrudRepository repository, PassportService passportService) {
        super(Flight.class, repository);
        this.passportService = passportService;
    }

    public List<FlightDTO> findAllFlightsByUserId(BigInteger userId, int page) {
        List<FlightDTO> flights = new ArrayList<>();
        List<TicketHistory> ticketsHistory = getTicketsHistoryByUserId(userId);
        List<Ticket> tickets = new ArrayList<>();
        Set<Flight> flightSet = new HashSet<>();
        for (TicketHistory ticketHistory:
             ticketsHistory) {
            Ticket ticket = getTicketById(ticketHistory.getTicketId()).get();
            tickets.add(ticket);
        }
        for (Ticket ticket:
             tickets) {
            Flight flight = getFlightById(ticket.getFlightId()).get();
            flightSet.add(flight);

        }
        List<Ticket> newTickets = new ArrayList<>();
        List<Passenger> newPassengers = new ArrayList<>();
        List<Passport> passports = new ArrayList<>();
        for (Flight flight:
             flightSet) {
            for (Ticket ticket:
                 tickets) {
                BigInteger id1 = ticket.getFlightId();
                BigInteger id2 = flight.getObjectId();
                if (id1.equals(id2)) {
                    newTickets.add(ticket);
                    newPassengers.add(getPassengerById(ticket.getPassengerId()).get());
                    Passport passport = passportService.findPassportByReference(ticket.getPassengerId());
                    passports.add(passport);
                }
            }
            Airport arrivalAirport = getAirportById(flight.getArrivalAirportId()).get();
            Airport departureAirport = getAirportById(flight.getDepartureAirportId()).get();
            Airplane airplane = getAirplaneById(flight.getAirplaneId()).get();
            Airline airline = getAirlineById(airplane.getAirlineId()).get();
            flights.add(new FlightDTO(flight, newTickets, newPassengers, passports, arrivalAirport, departureAirport, airplane, airline));
            newTickets = new ArrayList<>();
            newPassengers = new ArrayList<>();
            passports = new ArrayList<>();
        }

        return flights;
    }

    private List<TicketHistory> getTicketsHistoryByUserId(BigInteger userId) {
        return repository.findSliceOfReference(userId, TicketHistory.class);
    }

    private Optional<Ticket> getTicketById(BigInteger ticketId) {
        return repository.findById(ticketId, Ticket.class);
    }

    private Optional<Flight> getFlightById(BigInteger flightId) {
        return repository.findById(flightId, Flight.class);
    }

    private Optional<Passenger> getPassengerById(BigInteger flightId) {
        return repository.findById(flightId, Passenger.class);
    }

    private Optional<Airport> getAirportById(BigInteger airportId) {
        return repository.findById(airportId, Airport.class);
    }

    private Optional<Airplane> getAirplaneById(BigInteger airportId) {
        return repository.findById(airportId, Airplane.class);
    }

    private Optional<Airline> getAirlineById(BigInteger airportId) {
        return repository.findById(airportId, Airline.class);
    }
}
