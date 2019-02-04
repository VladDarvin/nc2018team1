package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Log4j2
public class CountryService extends AbstractService<Country>{

    public CountryService(EavCrudRepository<Country> repository) {
        super(Country.class, repository);
    }

    public Country findByName(String name) {
        BigInteger nameAttrId = new BigInteger("1");
        Country foundCountry;
        try {
            foundCountry = findByAttr(name, nameAttrId, Country.class);
        } catch (IllegalStateException e) {
            String message = "Found more than 1 country with the same unique attribute(id=" + nameAttrId + "): " + name;
            IllegalStateException exception = new IllegalStateException(message);
            log.error(message, exception);
            throw exception;
        }

        return foundCountry;
    }

    public Country findCountryByObjectId(BigInteger objectId) {
        return repository.findById(objectId, Country.class).get();
    }


}
