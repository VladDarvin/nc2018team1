package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.dto.FlightSearchWrapper;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        return flightBookingService.findOneWayFlights(page, wrapper.getDepartureCity(), wrapper.getDestinationCity(), wrapper.getDepartureDate());
    }
}
