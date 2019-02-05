package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.BookingTwoWaysDto;
import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.dto.FlightSearchWrapper;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        List<Airport> departureAirportsByAttr = airportService.findItemsByAttr(departureCity, BigInteger.valueOf(5), Airport.class);
        List<Airport> arrivalAirportsByAttr = airportService.findItemsByAttr(destinationCity, BigInteger.valueOf(5), Airport.class);
        Set<Flight> flights = new HashSet<>();
        for (Airport depArr:
                departureAirportsByAttr) {
            List<Flight> flightOfReference = flightService.repository.findSliceOfReference(depArr.getObjectId(), Flight.class);
            flights.addAll(flightOfReference);
        }

        for (Airport arvArr:
                arrivalAirportsByAttr) {
            List<Flight> flightOfReference = flightService.repository.findSliceOfReference(arvArr.getObjectId(), Flight.class);
            flights.addAll(flightOfReference);
        }

        Set<Flight> foundFlights = new HashSet<>();
        for (Flight flight:
                flights) {
            String dateFromFlight = "";
            LocalDateTime tempDate = flight.getExpectedDepartureDatetime();
            if (tempDate != null) {
                dateFromFlight = tempDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            String dateFromSearch = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (dateFromFlight.equals(dateFromSearch)) {
                foundFlights.add(flight);
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

//    public List<FlightDTO> findOneWayFlights (int page, String departureCity, String arrivalCity, LocalDateTime departureDate) {
//
//        City foundDepartureCity = this.cityService.findByName(departureCity);
//        if (foundDepartureCity == null) {
////            FIXME MAKE CONVENIENT EXCEPTION
//            throw new RuntimeException("city(departure) not found: " + departureCity);
//        }
//        /*if(foundDepartureCity == null || foundDepartureCity.getName().equals("")) {
//            throw new ItemNotFoundException("Can't find " + departureCity + " City");
//        }*/
//        City foundArrivalCity = this.cityService.findByName(arrivalCity);
//        if (foundArrivalCity == null) {
////            FIXME MAKE CONVENIENT EXCEPTION
//            throw new RuntimeException("city(arrival) not found: " + arrivalCity);
//        }
//
//        //found airport(s) of departure city
//        List<Airport> airportsOfDepartureCity = makeAirportFilterList(page, foundDepartureCity);
//
//        //found airport(s) of arrival city
//        List<Airport> airportsOfArrivalCity = makeAirportFilterList(page, foundArrivalCity);
//
//        //found flights that begins in the departure airport(s)
//        List<FilterEntity> filterFlights = new ArrayList<>();
//        for (Airport airport : airportsOfDepartureCity) {
//            FilterEntity filterFlight = new FilterEntity(BigInteger.valueOf(11), Collections.singleton(airport.getObjectId()));
//            filterFlights.add(filterFlight);
//        }
//        List<Flight> allFlightsFromDepartureCity = repository.findSlice(Flight.class, new Page(page), new ArrayList<>(), filterFlights);
//
//        //sort all flights by needed arrival airports
//        boolean cityHasAirport;
//        for (Flight flight : allFlightsFromDepartureCity) {
//            cityHasAirport = false;
//            for (Airport airport : airportsOfArrivalCity) {
//                if (flight.getArrivalAirportId().equals(airport.getObjectId())) {
//                    cityHasAirport = true;
//                    break;
//                }
//            }
//            if (!cityHasAirport) {
//                allFlightsFromDepartureCity.remove(flight);
//            }
//        }
//
//        //sort all left flights by departure date
//        for (Flight flight : allFlightsFromDepartureCity) {
//            if (!flight.getExpectedDepartureDatetime().equals(departureDate)) {
//                allFlightsFromDepartureCity.remove(flight);
//            }
//        }
//
//        if (allFlightsFromDepartureCity.isEmpty()) {
//                        //TODO
//        }
//
//        return flightService.getAllFlightsByListOfFlights(allFlightsFromDepartureCity);
//    }
//
//    //---------------------------
//
//    private List<Airport> makeAirportFilterList(int page, City city) {
//        FilterEntity filterPassedCity = new FilterEntity(BigInteger.valueOf(5), Collections.singleton(city.getName()));
//        List<FilterEntity> filterPassedCities = new ArrayList<>();
//        filterPassedCities.add(filterPassedCity);
//        List<Airport> airportsOfPassedCity = repository.findSlice(Airport.class, new Page(page), new ArrayList<>(), filterPassedCities);
//        return airportsOfPassedCity;

//    }
}
