package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.BookingTwoWaysDto;
import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.dto.FlightSearchWrapper;
import com.nc.airport.backend.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FlightBookingController {
    private FlightBookingService flightBookingService;

    @Autowired
    public FlightBookingController(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }

    @PostMapping("/flight-booking/search-one-way/page={page}")
    public List<FlightDTO> findFlights(@PathVariable(name = "page") int page,
                                                    @RequestBody FlightSearchWrapper wrapper) {
        return flightBookingService.findOneWayFlights(page,wrapper.getDepartureCity(), wrapper.getDestinationCity(), wrapper.getDepartureDate());
    }

    @PostMapping("/flight-booking/search-both/page={page}")
    public BookingTwoWaysDto findFlightsForTwoWays(@PathVariable(name = "page") int page,
                                                   @RequestBody FlightSearchWrapper wrapper) {
        return flightBookingService.findTwoWayFlights(page,wrapper.getDepartureCity(), wrapper.getDestinationCity(), wrapper.getDepartureDate(), wrapper.getReturnDate());
    }
}
