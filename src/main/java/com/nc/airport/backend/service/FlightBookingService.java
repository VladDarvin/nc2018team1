package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.City;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FlightBookingService extends AbstractService {

    private CityService cityService;
    private FlightService flightService;

    public FlightBookingService(EavCrudRepository repository, CityService cityService, FlightService flightService) {
        super(Flight.class, repository);
        this.cityService = cityService;
        this.flightService = flightService;

    }


    public List<FlightDTO> findOneWayFlights (int page, String departureCity, String arrivalCity, LocalDateTime departureDate) {

        City foundDepartureCity = this.cityService.findByName(departureCity);
        if (foundDepartureCity == null) {
//            FIXME MAKE CONVENIENT EXCEPTION
            throw new RuntimeException("city(departure) not found: " + departureCity);
        }
        /*if(foundDepartureCity == null || foundDepartureCity.getName().equals("")) {
            throw new FlightNotFoundException("Can't find " + departureCity + " City");
        }*/
        City foundArrivalCity = this.cityService.findByName(arrivalCity);
        if (foundArrivalCity == null) {
//            FIXME MAKE CONVENIENT EXCEPTION
            throw new RuntimeException("city(arrival) not found: " + arrivalCity);
        }

        //found airport(s) of departure city
        List<Airport> airportsOfDepartureCity = makeAirportFilterList(page, foundDepartureCity);

        //found airport(s) of arrival city
        List<Airport> airportsOfArrivalCity = makeAirportFilterList(page, foundArrivalCity);

        //found flights that begins in the departure airport(s)
        List<FilterEntity> filterFlights = new ArrayList<>();
        for (Airport airport : airportsOfDepartureCity) {
            FilterEntity filterFlight = new FilterEntity(BigInteger.valueOf(11), Collections.singleton(airport.getObjectId()));
            filterFlights.add(filterFlight);
        }
        List<Flight> allFlightsFromDepartureCity = repository.findSlice(Flight.class, new Page(page), new ArrayList<>(), filterFlights);

        //sort all flights by needed arrival airports
        boolean cityHasAirport;
        for (Flight flight : allFlightsFromDepartureCity) {
            cityHasAirport = false;
            for (Airport airport : airportsOfArrivalCity) {
                if (flight.getArrivalAirportId().equals(airport.getObjectId())) {
                    cityHasAirport = true;
                    break;
                }
            }
            if (!cityHasAirport) {
                allFlightsFromDepartureCity.remove(flight);
            }
        }

        //sort all left flights by departure date
        for (Flight flight : allFlightsFromDepartureCity) {
            if (!flight.getExpectedDepartureDatetime().equals(departureDate)) {
                allFlightsFromDepartureCity.remove(flight);
            }
        }

        if (allFlightsFromDepartureCity.isEmpty()) {
                        //TODO
        }

        return flightService.getAllFlightsByListOfFlights(allFlightsFromDepartureCity);
    }

    //---------------------------

    private List<Airport> makeAirportFilterList(int page, City city) {
        FilterEntity filterPassedCity = new FilterEntity(BigInteger.valueOf(5), Collections.singleton(city.getName()));
        List<FilterEntity> filterPassedCities = new ArrayList<>();
        filterPassedCities.add(filterPassedCity);
        List<Airport> airportsOfPassedCity = repository.findSlice(Airport.class, new Page(page), new ArrayList<>(), filterPassedCities);
        return airportsOfPassedCity;
    }

}
