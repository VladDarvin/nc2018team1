package com.nc.airport.backend.persistence.eav.entity2mutable.entity;

import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

@ToString
public class NoObjTypeEntityAndNoExtend {
    @ValueField(ID = "123")
    protected String s = "test";
}
