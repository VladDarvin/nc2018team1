package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

@Service
public class AirlineService extends AbstractService<Airline> {

    public AirlineService(EavCrudRepository<Airline> repository) {
        super(Airline.class, repository);
    }
}
