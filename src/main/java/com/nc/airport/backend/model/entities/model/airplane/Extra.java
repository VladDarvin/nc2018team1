package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "6")
@Getter
@Setter
@ToString(callSuper = true)
public class Extra extends BaseEntity {

    @ValueField(ID = "18")
    private BigInteger extraTypeId;

    @ValueField(ID = "19")
    private BigInteger airplaneId;
}
