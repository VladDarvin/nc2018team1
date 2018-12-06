package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@ObjectType(ID = "5")
public class Airplane extends BaseEntity {

    @ValueField(ID = "16")
    private String model;

    @ReferenceField(ID = "17")
    private BigInteger airlineId;

}
