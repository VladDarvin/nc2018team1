package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
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
        return airlineService.findAllEntities();
    }

    @RequestMapping(value = "/airlines/page={page}", method = RequestMethod.GET)
    public List<Airline> getTenAirlines(@PathVariable(name = "page") int page) {
        return airlineService.getTenEntities(page);
    }

    @PostMapping(value = "/airlines")
    public Airline addNewAirline(@RequestBody Airline airline) {
        return airlineService.addEntity(airline);
    }

    @PutMapping(value = "/airlines")
    public Airline editAirline(@RequestBody Airline airline) {
        return airlineService.addEntity(airline);
    }

    @DeleteMapping(value = "/airlines/{id}")
    public void deleteAirline(@PathVariable(name = "id") BigInteger id) {
        airlineService.deleteEntity(id);
    }

    @GetMapping(value = "/airlines/count")
    public BigInteger getCountOfAirlines() {
        return airlineService.getEntitiesAmount();
    }

    @GetMapping(value = "/airlines/count/search={searchString}")
    public BigInteger getCountOfAirlinesByFilter(@PathVariable(name = "searchString") String searchString) {
        return airlineService.getAmountOfFilteredEntities(searchString);
    }

    @RequestMapping(value = "/airlines/search/page={page}", method = RequestMethod.POST)
    public ResponseFilteringWrapper searchAirlines(@PathVariable(name = "page") int page,
                                                   @RequestBody SortingFilteringWrapper wrapper) {
        return airlineService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

}
