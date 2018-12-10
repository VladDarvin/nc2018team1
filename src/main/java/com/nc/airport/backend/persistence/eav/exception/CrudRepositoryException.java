package com.nc.airport.backend.persistence.eav.exception;

import com.nc.airport.backend.model.BaseEntity;

public class CrudRepositoryException extends RuntimeException {
    private BaseEntity entity;

    public CrudRepositoryException(String message, Throwable cause, BaseEntity entity) {
        super(message, cause);
        this.entity = entity;
    }

    public CrudRepositoryException(String message) {
        super(message);
    }

    public BaseEntity getEntity() {
        return entity;
    }
}
