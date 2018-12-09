package com.nc.airport.backend.persistence.eav.entity2mutable;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;

/**
 * A service that can convert mutables to childs of BaseEntity and backwards
 */
public interface Entity2Mutable {

    /**
     * Converts mutable into entity of supplied type
     *
     * @param mutable initial mutable
     * @param clazz   supplied type
     * @param <T>     extends BaseEntity
     * @return entity of supplied type
     */
    <T extends BaseEntity> T convertMutableToEntity(Mutable mutable, Class<T> clazz);

    /**
     * Converts entity into mutable
     *
     * @param entity initial entity
     * @return mutable that corresponds to supplied entity
     */
    Mutable convertEntityToMutable(BaseEntity entity);
}
