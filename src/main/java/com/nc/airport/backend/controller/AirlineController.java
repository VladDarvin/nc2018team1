package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.service.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @RequestMapping(value = "/users/page={page}", method = RequestMethod.GET)
    public List<Airline> getTenAirlines(@PathVariable(name = "page") int page) {
        return airlineService.getTenAirlines(page);
    }

    @PostMapping(value = "/airlines")
    public Airline addNewAirline(@RequestBody Airline airline) {
        return airlineService.addAirline(airline);
    }

    @PutMapping(value = "/airlines")
    public Airline editAirline(@RequestBody Airline airline) {
        return airlineService.addAirline(airline);
    }

    @DeleteMapping(value = "/airlines/{id}")
    public void deleteAirline(@PathVariable(name = "id") BigInteger id) {
        airlineService.deleteAirline(id);
    }

    @GetMapping(value = "/airlines/count")
    public BigInteger getCountOfAirlines() {
        return airlineService.getAirlinesAmount();
    }

    @RequestMapping(value = "/users/airlines/page={page}", method = RequestMethod.POST)
    public List<Airline> searchAirlines(@PathVariable(name = "page") int page, @RequestBody Map<BigInteger, Set<Object>> filtering, @RequestBody Map<BigInteger, Boolean> sorting) {
        return airlineService.filtrateAndSortAirlines(page, filtering, sorting);
    }
}
