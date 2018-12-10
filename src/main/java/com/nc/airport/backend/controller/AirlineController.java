package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AirlineController {
    private AirlineService airlineService;

    @Autowired
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping(path = "/airlines")
    public List<Airline> getAll() {
        return airlineService.findAllAirlines();
    }
}
