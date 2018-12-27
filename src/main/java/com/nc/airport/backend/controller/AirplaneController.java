package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.service.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
public class AirplaneController {
    private AirplaneService airplaneService;

    @Autowired
    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping(path = "/airplanes")
    public List<Airplane> getAll() {
        return airplaneService.findAllAirplanes();
    }

    @RequestMapping(value = "/airplanes/page={page}", method = RequestMethod.GET)
    public List<Airplane> getTenAirlines(@PathVariable(name = "page") int page) {
        return airplaneService.getTenAirplanes(page);
    }

    @PostMapping(value = "/airplanes")
    public Airplane addNewAirline(@RequestBody Airplane airplane) {
        return airplaneService.addAirplane(airplane);
    }

    @PutMapping(value = "/airplanes")
    public Airplane editAirline(@RequestBody Airplane airplane) {
        return airplaneService.addAirplane(airplane);
    }

    @DeleteMapping(value = "/airplanes/{id}")
    public void deleteAirline(@PathVariable(name = "id") BigInteger id) {
        airplaneService.deleteAirplane(id);
    }

    @GetMapping(value = "/airplanes/count")
    public BigInteger getCountOfAirlines() {
        return airplaneService.getAirplanesAmount();
    }

    @GetMapping(value = "/airplanes/count/search={searchString}")
    public BigInteger getCountOfAirlinesByFilter(@PathVariable(name = "searchString") String searchString) {
        return airplaneService.getAmountOfFiltrateAirplanes(searchString);
    }

    @RequestMapping(value = "/airplanes/search/page={page}", method = RequestMethod.POST)
    public List<Airplane> searchAirlines(@PathVariable(name = "page") int page,
                                        @RequestBody SortingFilteringWrapper wrapper) {
        return airplaneService.filterAndSortAirlines(page, wrapper.getSearchString(), wrapper.getSortList());
    }

}

