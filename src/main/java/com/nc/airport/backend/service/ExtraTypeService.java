package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.ExtraType;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ExtraTypeService extends AbstractService<ExtraType> {

    public ExtraTypeService(EavCrudRepository<ExtraType> repository) {
        super(ExtraType.class, repository);
    }
}
