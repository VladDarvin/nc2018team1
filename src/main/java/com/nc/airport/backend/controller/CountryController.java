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
    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/page={page}")
    public List<Country> getTenCountries(@PathVariable(name = "page") int page) {
        return countryService.getTenEntities(page);
    }

    @PostMapping
    public Country addNewCountry(@RequestBody Country country) {
        return countryService.addEntity(country);
    }

    @PutMapping
    public Country editCountry(@RequestBody Country country) {
        return countryService.addEntity(country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable(name = "id") BigInteger id) {
        countryService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfCountries() {
        return countryService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfCountriesByFilter(@PathVariable(name = "searchString") String searchString) {
        return countryService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchCountries(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return countryService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
