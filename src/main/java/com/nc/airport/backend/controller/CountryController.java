package com.nc.airport.backend.controller;

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
    public List<Country> getAll() {
        return countryService.findAllCountries();
    }

    @PostMapping(value = "/countries")
    public Country addNewCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @PutMapping(value = "/countries")
    public Country editCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @DeleteMapping(value = "/countries/{id}")
    public void deleteCountry(@PathVariable(name = "id") BigInteger id) {
        countryService.deleteCountry(id);
    }

    @GetMapping(value = "/countries/count")
    public BigInteger getCountOfCountries() {
        return countryService.getCountriesAmount();
    }
}
