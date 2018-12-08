package com.nc.airport.backend.persistence.eav.entity2mutable.builder;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;

public interface EntityBuilder {
    <T extends BaseEntity> T build(Class<T> clazz, Mutable mutable);
}
