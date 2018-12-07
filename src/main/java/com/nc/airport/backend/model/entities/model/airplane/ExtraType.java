package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "7")
public class ExtraType extends BaseEntity {

    @ValueField(ID = "20")
    private String name;

    @ValueField(ID = "21")
    private String descr;

    @ValueField(ID = "22")
    private double baseCost;
}
