package com.nc.airport.backend.service.exception;

import com.nc.airport.backend.model.BaseEntity;

public class PersistenceException extends RuntimeException {
    private BaseEntity entity;

    public PersistenceException(String message, BaseEntity entity) {
        super(message);
        this.entity = entity;
    }

    public BaseEntity getEntity() {
        return entity;
    }
}
