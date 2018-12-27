package com.nc.airport.backend.persistence.eav.entity2mutable.entity;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

@ToString
public class NoObjTypeEntity extends BaseEntity {
    @ValueField(ID = "123123")
    protected String test = "123";
}
