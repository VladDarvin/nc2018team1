package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class CountryService {
    private EavCrudRepository<Country> countriesRepository;

    @Autowired
    public CountryService(EavCrudRepository<Country> countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public List<Country> findAllCountries() {
        return countriesRepository.findAll(Country.class);
    }

    public Country addCountry(Country country) {
        return countriesRepository.save(country);
    }

    public void deleteCountry(BigInteger id) {
        countriesRepository.deleteById(id);
    }

    public BigInteger getCountriesAmount() {
        return countriesRepository.count(Country.class);
    }
}
