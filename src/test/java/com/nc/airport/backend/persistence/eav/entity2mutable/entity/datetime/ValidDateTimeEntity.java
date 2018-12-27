package com.nc.airport.backend.persistence.eav.entity2mutable.entity.datetime;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.time.LocalDateTime;

@ObjectType(ID = "1")
@ToString
public class ValidDateTimeEntity extends ValidNoFieldsEntity {
    @DateField(ID = "1")
    protected LocalDateTime ldt = LocalDateTime.now();
}
