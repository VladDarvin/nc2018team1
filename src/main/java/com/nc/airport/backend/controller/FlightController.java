package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.FlightService;
import com.nc.airport.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    private FlightService flightService;
    private UserService userService;

    @Autowired
    public FlightController(FlightService flightService, UserService userService) {
        this.flightService = flightService;
        this.userService = userService;
    }

    @RequestMapping(value = "/user-flights/userLogin={userLogin}/page={page}", method = RequestMethod.GET)
    public List<FlightDTO> getAllFlights(@PathVariable String userLogin, @PathVariable int page) {
        User parentUser = userService.findByLogin(userLogin);
        return flightService.findAllFlightsByUserId(parentUser.getObjectId(), page);
    }

    // --------------------------

    @RequestMapping(value = "/flights/page={page}", method = RequestMethod.GET)
    public List<FlightDTO> getAllFlights(@PathVariable int page) {
        return flightService.getAllFlights(page);
    }
}
