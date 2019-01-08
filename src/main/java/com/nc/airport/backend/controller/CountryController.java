package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/countries")
public class CountryController {
    private CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @GetMapping("/page={page}")
    public List<Country> getTenCountries(@PathVariable(name = "page") int page) {
        return service.getTenEntities(page);
    }

    @PostMapping
    public Country addNewCountry(@RequestBody Country country) {
        return service.saveEntity(country);
    }

    @PutMapping
    public Country editCountry(@RequestBody Country country) {
        return service.saveEntity(country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable(name = "id") BigInteger id) {
        service.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfCountries() {
        return service.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfCountriesByFilter(@PathVariable(name = "searchString") String searchString) {
        return service.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchCountries(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return service.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
