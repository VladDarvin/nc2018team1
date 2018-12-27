package com.nc.airport.backend.persistence.eav.entity2mutable.entity;


import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

@ObjectType(ID = "1")
@ToString
public class NotExtendingBaseEntEntity {
    @ValueField(ID = "1")
    protected String testString = "123";
}
