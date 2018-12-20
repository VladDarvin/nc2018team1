package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CountryService {
    private EavCrudRepository<Country> countriesRepository;

    @Autowired
    public CountryService(EavCrudRepository<Country> countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public List<Country> findAllCountries() {
        try {
            return countriesRepository.findSlice(Country.class, 1, getCountriesAmount().intValueExact());
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
        int offset = (page - 1) * 10;
        return countriesRepository.findSlice(Country.class, 1 + offset, 10 + offset);
    }

    public List<Country> filterAndSortCountries(int page,
                                                  Map<BigInteger, Set<Object>> filtering, Map<BigInteger, Boolean> sorting) {
        List<FilterEntity> filterEntities = null;
        if (filtering != null) {
            filterEntities = new ArrayList<>();
            for (BigInteger key :
                    filtering.keySet()) {
                filterEntities.add(new FilterEntity(key, filtering.get(key)));
            }
        }

        List<SortEntity> sortEntities = null;
        if (sorting != null) {
            sortEntities = new ArrayList<>();
            for (BigInteger key :
                    sorting.keySet()) {
                sortEntities.add(new SortEntity(key, sorting.get(key)));
            }
        }

        return countriesRepository.findSlice(Country.class, page * 10, page * 10 + 10, sortEntities, filterEntities);
    }
}
