package com.nc.airport.backend.persistence.eav.entity2mutable.entity.value;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;

@ObjectType(ID = "1")
public class ValidValueEntity extends ValidNoFieldsEntity {
    @ValueField(ID = "123")
    protected String name = "test";
}
