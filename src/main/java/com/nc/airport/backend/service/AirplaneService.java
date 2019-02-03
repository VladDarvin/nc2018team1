package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.dto.AirplaneDto;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.service.exception.ItemNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@Log4j2
public class AirplaneService extends AbstractService<Airplane> {

    private final AirlineService airlineService;

    @Autowired
    public AirplaneService(EavCrudRepository<Airplane> repository, AirlineService airlineService) {
        super(Airplane.class, repository);
        this.airlineService = airlineService;
    }

    public Airplane findAirplaneByObjectId(BigInteger objectId) {
        Optional<Airplane> result = repository.findById(objectId, Airplane.class);
        if (result.isPresent()) {
            return result.get();
        } else {
            String message = "Tried to find airplane that does not exist. Airplane id=" + objectId;
            RuntimeException ex = new ItemNotFoundException(message);
            log.warn(message, ex);
            throw ex;
        }
    }
}

