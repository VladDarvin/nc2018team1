package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
public class CountryController {
    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(path = "/countries")
    public List<Country> getAllCountries() {
        return countryService.findAllEntities();
    }

    @RequestMapping(value = "/countries/page={page}", method = RequestMethod.GET)
    public List<Country> getTenCountries(@PathVariable(name = "page") int page) {
        return countryService.getTenEntities(page);
    }

    @PostMapping(value = "/countries")
    public Country addNewCountry(@RequestBody Country country) {
        return countryService.addEntity(country);
    }

    @PutMapping(value = "/countries")
    public Country editCountry(@RequestBody Country country) {
        return countryService.addEntity(country);
    }

    @DeleteMapping(value = "/countries/{id}")
    public void deleteCountry(@PathVariable(name = "id") BigInteger id) {
        countryService.deleteEntity(id);
    }

    @GetMapping(value = "/countries/count")
    public BigInteger getCountOfCountries() {
        return countryService.getEntitiesAmount();
    }

    @GetMapping(value = "/countries/count/search={searchString}")
    public BigInteger getCountOfCountriesByFilter(@PathVariable(name = "searchString") String searchString) {
        return countryService.getAmountOfFilteredEntities(searchString);
    }

    @RequestMapping(value = "/countries/page={page}", method = RequestMethod.POST)
    public ResponseFilteringWrapper searchCountries(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return countryService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
