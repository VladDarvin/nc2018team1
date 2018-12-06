package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "6")
public class Extra {

    @ValueField(ID = "18")
    private int extraTypeId;

    @ValueField(ID = "19")
    private int airplaneId;


//    public int getExtraTypeId() {
//        return extraTypeId;
//    }
//
//    public void setExtraTypeId(int extraTypeId) {
//        this.extraTypeId = extraTypeId;
//    }
//
//    public int getAirplaneId() {
//        return airplaneId;
//    }
//
//    public void setAirplaneId(int airplaneId) {
//        this.airplaneId = airplaneId;
//    }
}
