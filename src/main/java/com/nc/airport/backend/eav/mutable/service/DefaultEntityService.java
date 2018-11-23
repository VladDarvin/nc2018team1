package com.nc.airport.backend.eav.mutable.service;

import com.nc.airport.backend.eav.mutable.Mutable;
import com.nc.airport.backend.eav.mutable.service.builder.EntityBuilder;
import com.nc.airport.backend.eav.mutable.service.parser.EntityParser;
import com.nc.airport.backend.model.BaseEntity;

public class DefaultEntityService implements EntityService {
    private EntityParser entityParser;
    private EntityBuilder entityBuilder;


    public DefaultEntityService(EntityParser entityParser, EntityBuilder entityBuilder) {
        this.entityParser = entityParser;
        this.entityBuilder = entityBuilder;
    }

    @Override
    public <T extends BaseEntity> T convertMutableToEntity(Mutable mutable, Class<T> entityClass) {
        T entity = entityBuilder.build(entityClass, mutable);
        throw new UnsupportedOperationException();
    }

    @Override
    public Mutable convertEntityToMutable(BaseEntity entity) {
        Mutable mutable = new Mutable();

        mutable.setObjectId(entity.getObjectId());
        mutable.setObjectTypeId(entityParser.parseObjectTypeId(entity));
        mutable.setParentId(entity.getParentId());
        mutable.setObjectName(entity.getObjectName());
        mutable.setObjectDescription(entity.getObjectDescription());
        mutable.setValues(entityParser.parseValues(entity));
        mutable.setDateValues(entityParser.parseDateValues(entity));
        mutable.setListValues(entityParser.parseListValues(entity));
        mutable.setReferences(entityParser.parseReferences(entity));

        return mutable;
    }
}
