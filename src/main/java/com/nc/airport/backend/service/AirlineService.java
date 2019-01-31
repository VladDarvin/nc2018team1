package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class AirlineService extends AbstractService<Airline> {

    public AirlineService(EavCrudRepository<Airline> repository) {
        super(Airline.class, repository);
    }

    public Airline findAirlineByObjectId(BigInteger objectId) {
        return repository.findById(objectId, Airline.class).get();
    }

    /**
     * <h3>WARNING</h3>
     * <p>The number of items returned is restricted to 2^32</p>
     * @return list of all items up to 2^32
     */
    public List<Airline> getAll() {
        int quantity = repository.count(Airline.class).intValue();
        Page page = new Page(quantity, 0);
        return repository.findSlice(Airline.class, page);
    }
}
