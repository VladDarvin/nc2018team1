package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.TicketDTO;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.*;

@Service
public class TicketService extends AbstractService {
    public TicketService(EavCrudRepository repository) {
        super(Ticket.class, repository);
    }

    public ResponseFilteringWrapper getTicketsInfo(String searchString, List<BigInteger> filters, int page) {
        List<Passenger> passengers = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();
        List<FilterEntity> filterEntities = new ArrayList<>();
        if (filters.size() == 2 && (filters.get(0).equals(BigInteger.valueOf(38)) || filters.get(0).equals(BigInteger.valueOf(39)))) {
            filterEntities = makeFilterList(searchString, Passenger.class);
            passengers = repository.findSlice(Passenger.class, new Page(page-1), null, filterEntities);
            if (passengers.size() == 0) {
                return new ResponseFilteringWrapper(Collections.emptyList(), BigInteger.valueOf(0));
            }
            List<FilterEntity> filterForTickets = makeFilterListForTicketsByPassenger(passengers);
            return searchItems(filterForTickets, page);
        } else {
            filterEntities = makeFilterList(searchString, Flight.class);
            flights = repository.findSlice(Flight.class, new Page(page-1), null, filterEntities);
            if (flights.size() == 0) {
                return new ResponseFilteringWrapper(Collections.emptyList(), BigInteger.valueOf(0));
            }
            List<FilterEntity> filterForTickets = makeFilterListForTicketsByFlight(flights);
            return searchItems(filterForTickets, page);
        }
    }

    private ResponseFilteringWrapper searchItems(List<FilterEntity> filterForTickets, int page) {
        List<TicketDTO> returnItems = new ArrayList<>();
        List<Ticket> tickets = repository.findSlice(Ticket.class, new Page(page-1), null, filterForTickets);
        BigInteger countOfPages = repository.count(Ticket.class, filterForTickets);
        for (Ticket ticket:
                tickets) {
            Flight flight = getFlightById(ticket.getFlightId()).get();
            Passenger passenger = getPassengerById(ticket.getPassengerId()).get();
            Optional<Passport> passport = getPassportById(passenger.getPassportId());
            Optional<Airport> arrivalAirport = getAirportById(flight.getArrivalAirportId());
            Optional<Airport> departureAirport = getAirportById(flight.getDepartureAirportId());
            Optional<Airplane> airplane = getAirplaneById(flight.getAirplaneId());
            Optional<Airline> airline = Optional.empty();
            if (airplane.isPresent()) {
                airline = getAirlineById(airplane.get().getAirlineId());
            }
            returnItems.add(new TicketDTO(ticket, flight, passenger, arrivalAirport.get(), departureAirport.get(), airplane.get(), airline.get(), passport.get()));
        }
        return new ResponseFilteringWrapper(returnItems, countOfPages);
    }

    private List<FilterEntity> makeFilterListForTicketsByPassenger(List<Passenger> filters) {
        List<FilterEntity> filterEntities = new ArrayList<>();
        for (Passenger entity :
                filters) {
            filterEntities.add(new FilterEntity(BigInteger.valueOf(32), new HashSet<>(Arrays.asList(entity.getObjectId()))));
        }
        return filterEntities;
    }

    private List<FilterEntity> makeFilterListForTicketsByFlight(List<Flight> filters) {
        List<FilterEntity> filterEntities = new ArrayList<>();
        for (Flight entity :
                filters) {
            filterEntities.add(new FilterEntity(BigInteger.valueOf(30), new HashSet<>(Arrays.asList(entity.getObjectId()))));
        }
        return filterEntities;
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

    private Optional<Passport> getPassportById(BigInteger passportId) {
        return repository.findById(passportId, Passport.class);
    }
}
