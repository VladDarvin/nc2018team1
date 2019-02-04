package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.model.entities.model.users.TicketHistory;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
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

    private Optional<Airline> getAirlineById(BigInteger airlineId) {
        return repository.findById(airlineId, Airline.class);
    }

    private List<FlightDTO> formFlightDTOs(List<Flight> flights) {
        List<FlightDTO> flightDTOs = new ArrayList<>();
        Map<BigInteger, Airplane> airplanes = new HashMap<>();
        Map<BigInteger, Airline> airlines = new HashMap<>();
        Map<BigInteger, Airport> airports = new HashMap<>();

        for (Flight flight : flights) {
            BigInteger departAirportId = flight.getDepartureAirportId();
            BigInteger arrivAirportId = flight.getArrivalAirportId();
            BigInteger airplaneId = flight.getAirplaneId();
            BigInteger airlineId = null;

            Airplane foundAirplane;
            if (!airplanes.containsKey(airplaneId)) {
                foundAirplane = getAirplaneById(airplaneId).orElse(null);
                airplanes.put(airplaneId, foundAirplane);
            } else {
                foundAirplane = airplanes.get(airplaneId);
            }

            if (foundAirplane != null) {
                airlineId = foundAirplane.getAirlineId();
                if (!airlines.containsKey(airlineId)) {
                    Airline foundAirline = getAirlineById(airlineId).orElse(null);
                    airlines.put(airlineId, foundAirline);
                }
            }

            if (!airports.containsKey(departAirportId)) {
                Airport foundAirport = getAirportById(departAirportId).orElse(null);
                airports.put(departAirportId, foundAirport);
            }

            if (!airports.containsKey(arrivAirportId)) {
                Airport foundAirport = getAirportById(arrivAirportId).orElse(null);
                airports.put(arrivAirportId, foundAirport);
            }


//            Airplane airplane = getAirplaneById(flight.getAirplaneId()).get();
//            Airline airline = getAirlineById(airplane.getAirlineId()).get();
//            Airport depAirport = getAirportById(flight.getDepartureAirportId()).get(),
//                    arrAirport = getAirportById(flight.getArrivalAirportId()).get();
//            flightDTOs.add(new FlightDTO(flight, null, null, null, arrAirport, depAirport, airplane, airline));

            FlightDTO flightDTO = new FlightDTO(flight, null, null, null,
                    airports.get(arrivAirportId),
                    airports.get(departAirportId),
                    airplanes.get(airplaneId),
                    airlines.get(airlineId));
            flightDTOs.add(flightDTO);

        }
        return flightDTOs;
    }

    // --------------------------

    public List<FlightDTO> getTenFlights(int page) {
        List<Flight> flights = repository.findSlice(Flight.class, new Page(page - 1));
        return formFlightDTOs(flights);
    }

    public ResponseFilteringWrapper searchFlights(int page, String searchRequest, List<SortEntity> sortEntities) {
        List<FilterEntity> filterFlights = makeFilterList(searchRequest, Flight.class);
        List<Flight> foundFlights = repository.findSlice(Flight.class, new Page(page - 1), sortEntities, filterFlights);
        List<FlightDTO> flightDTOs = formFlightDTOs(foundFlights);
        BigInteger countOfPages = repository.count(Flight.class, filterFlights);
        return new ResponseFilteringWrapper<>(flightDTOs, countOfPages);
    }

    public List<Airport> getAllAirports() {
        int itemsCount = repository.count(Airport.class).intValue();
        if (itemsCount == 0)
            return new ArrayList<>();
        Page page = new Page(0);
        List<Airport> airports = new ArrayList<>(repository.findSlice(Airport.class, page));
        while (airports.size() < itemsCount) {
            airports.addAll(repository.findSlice(Airport.class, page.next()));
        }
//        for (Airport airp : airports) {
//            System.err.println(airp.toString());
//        }
        return airports;
    }

    public List<Airplane> getAllAirplanesByAirport(BigInteger airportId) {
        List<Airplane> airplanes = repository.findSliceOfReference(airportId, Airplane.class);
        return airplanes;
    }

    // method for FlightBooking
    public List<FlightDTO> getAllFlightsByListOfFlights(List<Flight> flights) {
        return formFlightDTOs(flights);
    }
}
