package com.nc.airport.backend.persistence.eav.entity2mutable.impl;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.Entity2Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.builder.EntityBuilder;
import com.nc.airport.backend.persistence.eav.entity2mutable.parser.EntityParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultEntity2Mutable implements Entity2Mutable {
    private EntityParser entityParser;
    private EntityBuilder entityBuilder;

    @Autowired
    public DefaultEntity2Mutable(EntityParser entityParser, EntityBuilder entityBuilder) {
        this.entityParser = entityParser;
        this.entityBuilder = entityBuilder;
    }

    @Override
    public <T extends BaseEntity> T convertMutableToEntity(Mutable mutable, Class<T> entityClass) {
        return entityBuilder.build(entityClass, mutable);
    }

    @Override
    public Mutable convertEntityToMutable(BaseEntity entity) {
        Mutable mutable = new Mutable();

        mutable.setObjectId(entity.getObjectId());
        mutable.setObjectTypeId(entityParser.parseObjectTypeId(entity));
        mutable.setParentId(entity.getParentId());
        mutable.setObjectName(entityParser.generateObjectName(entity));
        mutable.setObjectDescription(entity.getObjectDescription());

        mutable.setValues(entityParser.parseValues(entity));
        mutable.setDateValues(entityParser.parseDateValues(entity));
        mutable.setListValues(entityParser.parseListValues(entity));
        mutable.setReferences(entityParser.parseReferences(entity));

        return mutable;
    }
}
