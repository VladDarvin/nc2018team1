package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "8")
@Getter
@Setter
@ToString(callSuper = true)
public class Seat extends BaseEntity {

    @ReferenceField(ID = "23")
    private BigInteger airplaneId;

    @ReferenceField(ID = "24")
    private BigInteger seatTypeId;

    @ValueField(ID = "25")
    private int row;

    @ValueField(ID = "26")
    private int col;
}
