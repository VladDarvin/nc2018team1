package com.nc.airport.backend.persistence.eav.entity2mutable.entity.datetime;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.util.Date;

@ObjectType(ID = "1")
@ToString
public class WrongTypeDateTimeEntity extends ValidNoFieldsEntity {
    @DateField(ID = "123")
    protected Date ldt = new Date();

    @DateField(ID = "124")
    protected String bad = "indeed";
}
