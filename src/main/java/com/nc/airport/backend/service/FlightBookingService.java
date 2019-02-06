package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.BookingTwoWaysDto;
import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.flight.FlightStatus;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FlightBookingService extends AbstractService {

    private CityService cityService;
    private FlightService flightService;
    private AirportService airportService;

    public FlightBookingService(EavCrudRepository repository, CityService cityService, FlightService flightService,
                                AirportService airportService) {
        super(Flight.class, repository);
        this.cityService = cityService;
        this.flightService = flightService;
        this.airportService = airportService;
    }

    public List<FlightDTO> findOneWayFlights (int page, String departureCity, String destinationCity, LocalDateTime date) {
        List<Airport> departureAirports = airportService.findItemsByAttr(departureCity, BigInteger.valueOf(5), Airport.class);
        List<Airport> destinationAirports = airportService.findItemsByAttr(destinationCity, BigInteger.valueOf(5), Airport.class);

        Set<Object> departureValues = new HashSet<>();
        for (Airport airport:
                departureAirports) {
            departureValues.add(airport.getObjectId());
        }
        FilterEntity departureAirportsFilter = new FilterEntity(BigInteger.valueOf(11), departureValues);

        Set<Object> destinationValues = new HashSet<>();
        for (Airport airport:
                destinationAirports) {
            destinationValues.add(airport.getObjectId());
        }
        FilterEntity destinationValuesAirportsFilter = new FilterEntity(BigInteger.valueOf(10), destinationValues);
        List<FilterEntity> filterEntities = new ArrayList<>();
        filterEntities.add(departureAirportsFilter);
        filterEntities.add(destinationValuesAirportsFilter);

        List<Flight> flights = flightService.repository.findSliceOfSeveralReferences(filterEntities, Flight.class);

        Set<Flight> foundFlights = new HashSet<>();
        for (Flight flight:
             flights) {
            if (flight.getStatus().equals(FlightStatus.SCHEDULED)) {
                String dateFromFlight = flight.getExpectedDepartureDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String dateFromSearch = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (dateFromFlight.equals(dateFromSearch)) {
                    foundFlights.add(flight);
                }
            }
        }

        return flightService.formFlightDTOs(new ArrayList<>(foundFlights));
    }

    public BookingTwoWaysDto findTwoWayFlights (int page, String departureCity, String destinationCity, LocalDateTime departureDate, LocalDateTime returnDate) {
        List<FlightDTO> departureFlights = findOneWayFlights(page, departureCity, destinationCity, departureDate);
        List<FlightDTO> flightsBack = findOneWayFlights(page, destinationCity, departureCity, returnDate);

        if (departureFlights.size() == 0 || flightsBack.size() == 0) {
            return new BookingTwoWaysDto();
        } else {
            return new BookingTwoWaysDto(departureFlights, flightsBack);
        }
    }
}
