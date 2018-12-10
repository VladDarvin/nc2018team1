package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.repository.EavCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineService {
    private EavCrudRepository<Airline> airlinesRepository;

    @Autowired
    public AirlineService(EavCrudRepository<Airline> airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

    public List<Airline> findAllAirlines() {
        return airlinesRepository.findAll(Airline.class);
    }
}
