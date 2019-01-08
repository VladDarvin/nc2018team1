package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "19")
@Getter
@Setter
@ToString(callSuper = true)
public class City extends BaseEntity {

    @ValueField(ID = "60")
    private String name;

    @ReferenceField(ID = "61")
    private BigInteger countryId;
}
