package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService extends AbstractService<Airplane>{

    public AirplaneService(EavCrudRepository<Airplane> repository) {
        super(Airplane.class, repository);
    }
}

