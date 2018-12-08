package com.nc.airport.backend.persistence.eav.entity2mutable;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;

/**
 * A service that can convert mutables to childs of BaseEntity and backwards
 */
public interface Entity2Mutable {

    <T extends BaseEntity> T convertMutableToEntity(Mutable mutable, Class<T> clazz);

    Mutable convertEntityToMutable(BaseEntity entity);
}
