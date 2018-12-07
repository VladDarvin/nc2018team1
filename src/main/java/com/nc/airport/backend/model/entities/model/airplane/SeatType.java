package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "9")
public class SeatType extends BaseEntity {

    @ValueField(ID = "27")
    private String name;

    @ValueField(ID = "28")
    private String descr;

    @ValueField(ID = "29")
    private double baseCost;
}
