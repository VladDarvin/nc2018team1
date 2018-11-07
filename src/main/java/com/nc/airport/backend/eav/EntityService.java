package com.nc.airport.backend.eav;

import com.nc.airport.backend.model.Entity;

//TODO JAVADOC
public interface EntityService<T extends Entity> {

    T convertMutableToEntity(Mutable mutable, Class<T> clazz);

    Mutable convertEntityToMutable(T entity);
}
