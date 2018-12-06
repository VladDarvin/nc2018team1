package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;

@ObjectType(ID = "6")
public class Extra extends BaseEntity {

    @ValueField(ID = "19")
    private int extraTypeId;

    @ValueField(ID = "20")
    private int airplaneId;


    public int getExtraTypeId() {
        return extraTypeId;
    }

    public void setExtraTypeId(int extraTypeId) {
        this.extraTypeId = extraTypeId;
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }
}
