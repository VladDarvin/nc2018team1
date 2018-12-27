package com.nc.airport.backend.persistence.eav.entity2mutable.entity.reference;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "1")
@ToString
public class ValidReferenceEntity extends ValidNoFieldsEntity {
    @ReferenceField(ID = "123")
    protected BigInteger id = new BigInteger("12");
}
