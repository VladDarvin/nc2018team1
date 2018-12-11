package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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

    @RequestMapping(value = "/airlines", method = RequestMethod.POST)
    public Airline addNewAirline(@RequestBody Airline airline) {
        return airlineService.addAirline(airline);
    }

    @RequestMapping(value = "/airlines", method = RequestMethod.PUT)
    public Airline editAirline(@RequestBody Airline airline) {
        return airlineService.addAirline(airline);
    }

    @RequestMapping(value = "/airlines/{id}", method = RequestMethod.DELETE)
    public void deleteAirline(@PathVariable(name = "id") BigInteger id) {
        airlineService.deleteAirline(id);
    }

    @RequestMapping(value = "/airlines/count", method = RequestMethod.GET)
    public BigInteger getCountOfAirlines() {
        return airlineService.getAirlinesAmount();
    }
}
