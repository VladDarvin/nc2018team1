package com.nc.airport.backend.model;

public interface EntityFactory {

    <T extends Entity> T getNewEntity(Class<T> clazz);

}
