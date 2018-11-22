package com.nc.airport.backend.eav.mutable.service.builder;

import com.nc.airport.backend.eav.mutable.Mutable;
import com.nc.airport.backend.model.BaseEntity;

public interface EntityBuilder {
    <T extends BaseEntity> T build(Class<T> clazz, Mutable mutable);
}
