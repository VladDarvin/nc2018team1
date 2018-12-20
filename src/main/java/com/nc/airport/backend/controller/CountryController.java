package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @RequestMapping(value = "/countries/page={page}", method = RequestMethod.GET)
    public List<Country> getTenCountries(@PathVariable(name = "page") int page) {
        return countryService.getTenCountries(page);
    }

    @RequestMapping(value = "/countries/page={page}", method = RequestMethod.POST)
    public List<Country> searchCountries(@PathVariable(name = "page") int page,
                                         @RequestBody Map<BigInteger, Set<Object>> filtering, @RequestBody Map<BigInteger, Boolean> sorting) {
        return countryService.filterAndSortCountries(page, filtering, sorting);
    }
}
