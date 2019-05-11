package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class PassportService extends AbstractService<Passport> {

    public PassportService(EavCrudRepository<Passport> repository) {
        super(Passport.class, repository);
    }

    public Passport findPassportByReference(BigInteger objectId) {
        return repository.findEntityByReference(objectId, Passport.class);
    }
}
