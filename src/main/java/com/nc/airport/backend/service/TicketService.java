package com.nc.airport.backend.service;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.TicketDTO;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.model.entities.model.ticketinfo.TicketStatus;
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

    /**
     * <h3>WARNING</h3>
     * we expect page is one-based
     *
     * @param searchString
     * @param filters
     * @param page
     * @return
     */
    public ResponseFilteringWrapper getTicketsInfo(String searchString, List<BigInteger> filters, int page) {
        page--;
        final BigInteger passengerFirstNameAttrId = BigInteger.valueOf(38);
        final BigInteger passengerLastNameAttrId = BigInteger.valueOf(39);

        if (filters.size() == 2 &&
                (filters.get(0).equals(passengerFirstNameAttrId) || filters.get(0).equals(passengerLastNameAttrId))) {
            return searchByClass(Passenger.class, searchString, page);
        } else {
            return searchByClass(Flight.class, searchString, page);
        }
    }

    private ResponseFilteringWrapper searchByClass(Class<? extends BaseEntity> clazz, String searchString, int page) {
        List<FilterEntity> filterEntities = makeFilterList(searchString, clazz);
        List<? extends BaseEntity> entities = repository.findSlice(clazz, new Page(page), null, filterEntities);
        if (entities.size() == 0) {
            return new ResponseFilteringWrapper(Collections.emptyList(), BigInteger.valueOf(0));
        }
        List<FilterEntity> filterForTickets = makeFilterListForTickets(entities, clazz);
        return searchItems(filterForTickets, page);
    }

    private ResponseFilteringWrapper searchItems(List<FilterEntity> filterForTickets, int page) {
        List<TicketDTO> returnItems = new ArrayList<>();
        List<Ticket> tickets = repository.findSlice(Ticket.class, new Page(page - 1), null, filterForTickets);
        BigInteger countOfPages = repository.count(Ticket.class, filterForTickets);
        for (Ticket ticket :
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

    private List<FilterEntity> makeFilterListForTickets(List<? extends BaseEntity> filters, Class<? extends BaseEntity> clazz) {
        List<FilterEntity> filterEntities = new ArrayList<>();
        final BigInteger ticketFlightIdAttrId = BigInteger.valueOf(30);
        final BigInteger ticketPassengerIdAttrId = BigInteger.valueOf(32);
        BigInteger attrId;
        if (clazz == Passenger.class) {
            attrId = ticketPassengerIdAttrId;
        } else {
            attrId = ticketFlightIdAttrId;
        }
        for (BaseEntity entity : filters) {
            filterEntities.add(new FilterEntity(attrId, new HashSet<>(Arrays.asList(entity.getObjectId()))));
        }
        return filterEntities;
    }

    public List<Ticket> getAllByFlightId(BigInteger flightId) {
        return repository.findSliceOfReference(flightId, Ticket.class);
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

    public List<Ticket> saveAll(List<Ticket> tickets) {
        List<Ticket> savedTickets = repository.saveAll(tickets);

        if (tickets.size() > 0) {
            TicketStatus ticketStatus = tickets.get(0).getTicketStatus();
            if (ticketStatus == TicketStatus.NEW) {
            }
        }
        return savedTickets;
    }
}
