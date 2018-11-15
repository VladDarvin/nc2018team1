package com.nc.airport.backend.model;

public interface EntityFactory {

    <T extends BaseEntity> T getNewEntity(Class<T> clazz);
}
