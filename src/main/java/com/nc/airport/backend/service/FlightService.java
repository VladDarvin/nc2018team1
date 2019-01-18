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
import com.nc.airport.backend.persistence.eav.repository.Page;
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
        for (TicketHistory ticketHistory :
                ticketsHistory) {
            Optional<Ticket> ticket = getTicketById(ticketHistory.getTicketId());
            ticket.ifPresent(tickets::add);
        }
        for (Ticket ticket :
                tickets) {
            Optional<Flight> flight = getFlightById(ticket.getFlightId());
            flight.ifPresent(flightSet::add);
        }
        List<Ticket> newTickets = new ArrayList<>();
        List<Passenger> newPassengers = new ArrayList<>();
        List<Passport> passports = new ArrayList<>();
        for (Flight flight :
                flightSet) {
            for (Ticket ticket :
                    tickets) {
                BigInteger id1 = ticket.getFlightId();
                BigInteger id2 = flight.getObjectId();
                if (id1.equals(id2)) {
                    newTickets.add(ticket);
                    Optional<Passenger> passenger = getPassengerById(ticket.getPassengerId());
                    passenger.ifPresent(newPassengers::add);
                    Passport passport = passportService.findPassportByReference(ticket.getPassengerId());
                    passports.add(passport);
                }
            }

            Optional<Airport> arrivalAirport = getAirportById(flight.getArrivalAirportId());
            Optional<Airport> departureAirport = getAirportById(flight.getDepartureAirportId());
            Optional<Airplane> airplane = getAirplaneById(flight.getAirplaneId());
            Optional<Airline> airline = Optional.empty();
            if (airplane.isPresent()) {
                airline = getAirlineById(airplane.get().getAirlineId());
            }
            if (arrivalAirport.isPresent() &&
                    departureAirport.isPresent() &&
                    airplane.isPresent() &&
                    airline.isPresent())
                flights.add(new FlightDTO(flight, newTickets, newPassengers, passports,
                                        arrivalAirport.get(), departureAirport.get(), airplane.get(), airline.get()));
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

    // --------------------------

    public List<FlightDTO> getAllFlights(int page) {
        List<Flight> flights = repository.findSlice(Flight.class, new Page(page));
        List<FlightDTO> flightDTOs = new ArrayList<>();
        for (Flight flight : flights) {
            Airplane airplane = getAirplaneById(flight.getAirplaneId()).get();
            Airline airline = getAirlineById(airplane.getAirlineId()).get();
            Airport depAirport = getAirportById(flight.getDepartureAirportId()).get(),
                    arrAirport = getAirportById(flight.getArrivalAirportId()).get();
            flightDTOs.add(new FlightDTO(flight, null, null, null, arrAirport, depAirport, airplane, airline));
        }
        return flightDTOs;
    }
}
