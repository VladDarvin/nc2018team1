package com.nc.airport.backend.persistence.eav.entity2mutable.entity.reference;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

@ObjectType(ID = "1")
@ToString
public class WrongTypeReferenceEntity extends ValidNoFieldsEntity {
    @ReferenceField(ID = "123")
    protected int i = 5;
}
