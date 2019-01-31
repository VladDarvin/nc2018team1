package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.dto.AirplaneDto;
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


    @GetMapping("/page={page}")
    public List<Airplane> getTenAirplanes(@PathVariable(name = "page") int page) {
        return airplaneService.getTenEntities(page);
    }

    @GetMapping("/objectId={objectId}")
    public Airplane getAirplaneById(@PathVariable(name = "objectId") BigInteger objectId) {
        return airplaneService.findAirplaneByObjectId(objectId);
    }

    @PostMapping
    public Airplane addNewAirplane(@RequestBody AirplaneDto airplaneDto) {
        return airplaneService.updateEntity(new Airplane(airplaneDto));
    }

    @PutMapping
    public Airplane editAirplane(@RequestBody AirplaneDto airplaneDto) {
        return airplaneService.updateEntity(new Airplane(airplaneDto));
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

