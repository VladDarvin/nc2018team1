package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class SeatTypeService extends AbstractService<SeatType> {
    public SeatTypeService(EavCrudRepository repository) {
        super(SeatType.class, repository);
    }

    public Optional<SeatType> getSeatTypeById(BigInteger objectId) {
        return repository.findById(objectId, SeatType.class);
    }
}
