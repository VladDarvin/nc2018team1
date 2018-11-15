package com.nc.airport.backend.eav.mutable.service;

import com.nc.airport.backend.eav.mutable.Mutable;
import com.nc.airport.backend.model.BaseEntity;

/**
 * A service that can convert mutables to childs of BaseEntity and backwards
 * @param <T>
 */
public interface EntityService<T extends BaseEntity> {

    T convertMutableToEntity(Mutable mutable, Class<T> clazz);

    Mutable convertEntityToMutable(T entity);
}
