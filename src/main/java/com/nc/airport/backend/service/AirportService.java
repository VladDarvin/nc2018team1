package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService extends AbstractService<Airport> {
    public AirportService(EavCrudRepository<Airport> repository) {
        super(Airport.class, repository);
    }

    public List<Airport> getAll() {
        int quantity = repository.count(Airport.class).intValue();
        Page page = new Page(quantity, 0);
        return repository.findSlice(Airport.class, page);
    }
}
