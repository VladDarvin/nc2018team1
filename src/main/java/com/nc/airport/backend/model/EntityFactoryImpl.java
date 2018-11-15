package com.nc.airport.backend.model;

public class EntityFactoryImpl implements EntityFactory {
    @Override
    public <T extends BaseEntity> T getNewEntity(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
