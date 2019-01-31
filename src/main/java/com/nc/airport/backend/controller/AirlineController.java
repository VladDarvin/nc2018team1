package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.service.AirlineService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/airlines")
public class AirlineController {

    private AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @GetMapping("/page={page}")
    public List<Airline> getTenAirlines(@PathVariable(name = "page") int page) {
        return airlineService.getTenEntities(page);
    }

    @GetMapping("/objectId={objectId}")
    public Airline getAirlineById(@PathVariable(name = "objectId") BigInteger objectId) {
        return airlineService.findAirlineByObjectId(objectId);
    }

    @PostMapping
    public Airline addNewAirline(@RequestBody Airline airline) {
        return airlineService.updateEntity(airline);
    }

    @PutMapping
    public Airline editAirline(@RequestBody Airline airline) {
        return airlineService.updateEntity(airline);
    }

    @DeleteMapping("/{id}")
    public void deleteAirline(@PathVariable(name = "id") BigInteger id) {
        airlineService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfAirlines() {
        return airlineService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfAirlinesByFilter(@PathVariable(name = "searchString") String searchString) {
        return airlineService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchAirlines(@PathVariable(name = "page") int page,
                                                   @RequestBody SortingFilteringWrapper wrapper) {
        return airlineService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

    @GetMapping
    public List<Airline> getAll() {
        return airlineService.getAll();
    }

}
