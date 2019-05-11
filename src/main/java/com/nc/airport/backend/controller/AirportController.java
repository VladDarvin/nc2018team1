package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.AirportDto;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.service.AirportService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/airports")
public class AirportController {
    private AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/page={page}")
    public List<AirportDto> getTenAirports(@PathVariable(name = "page") int page) {
        return airportService.getTenDtoEntities(page);
    }

    @PostMapping
    public Airport addNewAirport(@RequestBody Airport airport) {
        return airportService.updateEntity(airport);
    }

    @PutMapping
    public Airport editAirport(@RequestBody Airport airport) {
        return airportService.updateEntity(airport);
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable(name = "id") BigInteger id) {
        airportService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfAirports() {
        return airportService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfAirportsByFilter(@PathVariable(name = "searchString") String searchString) {
        return airportService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchAirports(@PathVariable(name = "page") int page,
                                                   @RequestBody SortingFilteringWrapper wrapper) {
        ResponseFilteringWrapper<Airport> entities = airportService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
        return airportService.getDtoBySearchingResult(entities);
    }

    @GetMapping("/searchCityName/cityName={cityName}")
    public List<Airport> searchCityName(@PathVariable(name = "cityName") String cityName) {
        return airportService.findCityNames(cityName);
    }

    @GetMapping
    public List<Airport> getAll() {
        return airportService.getAll();
    }
}
