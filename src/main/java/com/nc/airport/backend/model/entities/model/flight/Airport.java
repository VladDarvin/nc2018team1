package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@ObjectType(ID = "2")
public class Airport extends BaseEntity {

    @ValueField(ID = "2")
    private String name;

    @ReferenceField(ID = "3")
    private BigInteger countryId;

    @ValueField(ID = "4")
    private String address;

    @ValueField(ID = "5")
    private String city;
}
