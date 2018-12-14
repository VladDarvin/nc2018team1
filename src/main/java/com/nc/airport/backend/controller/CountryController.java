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

    @RequestMapping(value = "/countries", method = RequestMethod.POST)
    public Country addNewCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @RequestMapping(value = "/countries", method = RequestMethod.PUT)
    public Country editCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @RequestMapping(value = "/countries/{id}", method = RequestMethod.DELETE)
    public void deleteCountry(@PathVariable(name = "id") BigInteger id) {
        countryService.deleteCountry(id);
    }

    @RequestMapping(value = "/countries/count", method = RequestMethod.GET)
    public BigInteger getCountOfCountries() {
        return countryService.getCountriesAmount();
    }
}
