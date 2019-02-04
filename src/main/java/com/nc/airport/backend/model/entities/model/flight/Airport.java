package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "2")
@Getter
@Setter
@ToString(callSuper = true)
public class Airport extends BaseEntity {

    @ValueField(ID = "2")
    private String name;

    @ReferenceField(ID = "3")
    private BigInteger country;

    @ValueField(ID = "4")
    private String address;

    @ValueField(ID = "5")
    private String city;
}
