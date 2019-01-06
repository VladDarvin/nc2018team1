package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends AbstractService<Country>{

    public CountryService(EavCrudRepository<Country> repository) {
        super(Country.class, repository);
    }
}
