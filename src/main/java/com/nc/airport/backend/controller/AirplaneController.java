package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.service.AirplaneService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/airplanes")
public class AirplaneController {
    private AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping
    public List<Airplane> getAll() {
        return airplaneService.findAllEntities();
    }

    @GetMapping("/page={page}")
    public List<Airplane> getTenAirplanes(@PathVariable(name = "page") int page) {
        return airplaneService.getTenEntities(page);
    }

    @PostMapping
    public Airplane addNewAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addEntity(airplane);
    }

    @PutMapping
    public Airplane editAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addEntity(airplane);
    }

    @DeleteMapping("/{id}")
    public void deleteAirplane(@PathVariable(name = "id") BigInteger id) {
        airplaneService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfAirplanes() {
        return airplaneService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfAirplanesByFilter(@PathVariable(name = "searchString") String searchString) {
        return airplaneService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchAirplanes(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return airplaneService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

}

