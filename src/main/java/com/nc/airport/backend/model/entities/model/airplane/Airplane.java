package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "5")
public class Airplane {

    @ValueField(ID = "16")
    private String model;

    @ReferenceField(ID = "17")
    private int airlineId;

//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public int getAirlineId() {
//        return airlineId;
//    }
//
//    public void setAirlineId(int airlineId) {
//        this.airlineId = airlineId;
//    }
}
