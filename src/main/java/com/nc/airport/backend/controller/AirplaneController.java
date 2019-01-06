package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
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
        return airplaneService.findAllEntities();
    }

    @RequestMapping(value = "/airplanes/page={page}", method = RequestMethod.GET)
    public List<Airplane> getTenAirplanes(@PathVariable(name = "page") int page) {
        return airplaneService.getTenEntities(page);
    }

    @PostMapping(value = "/airplanes")
    public Airplane addNewAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addEntity(airplane);
    }

    @PutMapping(value = "/airplanes")
    public Airplane editAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addEntity(airplane);
    }

    @DeleteMapping(value = "/airplanes/{id}")
    public void deleteAirplane(@PathVariable(name = "id") BigInteger id) {
        airplaneService.deleteEntity(id);
    }

    @GetMapping(value = "/airplanes/count")
    public BigInteger getCountOfAirplanes() {
        return airplaneService.getEntitiesAmount();
    }

    @GetMapping(value = "/airplanes/count/search={searchString}")
    public BigInteger getCountOfAirplanesByFilter(@PathVariable(name = "searchString") String searchString) {
        return airplaneService.getAmountOfFilteredEntities(searchString);
    }

    @RequestMapping(value = "/airplanes/search/page={page}", method = RequestMethod.POST)
    public ResponseFilteringWrapper searchAirplanes(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return airplaneService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

}

