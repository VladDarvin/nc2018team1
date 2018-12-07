package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@ObjectType(ID = "6")
public class Extra extends BaseEntity {

    @ValueField(ID = "18")
    private BigInteger extraTypeId;

    @ValueField(ID = "19")
    private BigInteger airplaneId;
}
