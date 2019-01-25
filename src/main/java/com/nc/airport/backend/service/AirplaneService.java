package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AirplaneService extends AbstractService<Airplane>{

    public AirplaneService(EavCrudRepository<Airplane> repository) {
        super(Airplane.class, repository);
    }

    public Airplane findAirplaneByObjectId(BigInteger objectId) {
        return repository.findById(objectId, Airplane.class).get();
    }
}

