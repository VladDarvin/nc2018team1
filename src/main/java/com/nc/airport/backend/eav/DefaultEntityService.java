package com.nc.airport.backend.eav;

import com.nc.airport.backend.model.Entity;
import com.nc.airport.backend.model.EntityFactory;

public class DefaultEntityService<T extends Entity> implements EntityService<T> {
    private EntityFactory entityFactory;
    private MutableFactory mutableFactory;
    private EntityParser<T> entityParser;

    public DefaultEntityService(EntityFactory entityFactory, MutableFactory mutableFactory, EntityParser<T> entityParser) {
        this.entityFactory = entityFactory;
        this.mutableFactory = mutableFactory;
        this.entityParser = entityParser;
    }

    @Override
    public T convertMutableToEntity(Mutable mutable, Class<T> entityClass) {
        T entity = entityFactory.getNewEntity(entityClass);

        entity.setId(mutable.getObjectId());
        entity.setParentId(mutable.getParentId());
        entity.fillAttributesFromMap(mutable.getAttributes());
        entity.fillReferencesFromMap(mutable.getReferences());

        return entity;
    }

    @Override
    public Mutable convertEntityToMutable(T entity) {
        Mutable mutable = mutableFactory.getNewMutable();

        mutable.setObjectId(entity.getId());
        mutable.setObjectTypeId(entityParser.parseObjectTypeId(entity));
        mutable.setParentId(entity.getParentId());
        mutable.setAttributes(entityParser.parseAttributes(entity));
        mutable.setReferences(entityParser.parseReferences(entity));

        return mutable;
    }
}
