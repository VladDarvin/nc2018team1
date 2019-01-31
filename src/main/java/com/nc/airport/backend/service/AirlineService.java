package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AirlineService extends AbstractService<Airline> {

    public AirlineService(EavCrudRepository<Airline> repository) {
        super(Airline.class, repository);
    }

    public Airline findAirlineByObjectId(BigInteger objectId) {
        return repository.findById(objectId, Airline.class).get();
    }
}
