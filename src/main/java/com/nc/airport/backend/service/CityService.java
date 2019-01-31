package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.City;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Log4j2
public class CityService extends AbstractService<City>{

    public CityService(EavCrudRepository<City> repository) {
        super(City.class, repository);
    }

    public City findByName(String name) {
        BigInteger nameAttrId = new BigInteger("19");
        City foundCity;
        try {
            foundCity = findByAttr(name, nameAttrId, City.class);
        } catch (IllegalStateException e) {
            String message = "Found more than 1 city with the same unique attribute(id=" + nameAttrId + "): " + name;
            IllegalStateException exception = new IllegalStateException(message);
            log.error(message, exception);
            throw exception;
        }

        return foundCity;
    }


}