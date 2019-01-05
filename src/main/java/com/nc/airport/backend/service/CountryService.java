package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService extends AbstractService{
    private EavCrudRepository<Country> countriesRepository;

    @Autowired
    public CountryService(EavCrudRepository<Country> countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public List<Country> findAllCountries() {
        try {
            return countriesRepository.findSlice(Country.class, new Page(getCountriesAmount().intValueExact(), 0));
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Amount of countries is greater than int range");
        }
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

    public List<Country> getTenCountries(int page) {
        return countriesRepository.findSlice(Country.class, new Page(page - 1));
    }

    public BigInteger getAmountOfFilteredCountries(String searchString) {
        List<FilterEntity> filterBy = makeFilterList(searchString, Country.class);
        return countriesRepository.count(Country.class, filterBy);
    }

    public ResponseFilteringWrapper filterAndSortCountries(int page, String search, List<SortEntity> sortEntities) {
        List<FilterEntity> filterEntities = makeFilterList(search, Country.class);
        List<Country> countries = countriesRepository.findSlice(Country.class, new Page(page - 1), sortEntities, filterEntities);
        List<Object> entities = new ArrayList<>(countries);
        BigInteger countOfPages = countriesRepository.count(Country.class, filterEntities);
        return new ResponseFilteringWrapper(entities, countOfPages);
    }


}
