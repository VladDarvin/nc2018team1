package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "9")
public class SeatType {

    @ValueField(ID = "27")
    private String name;

    @ValueField(ID = "28")
    private String descr;

    @ValueField(ID = "29")
    private double baseCost;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescr() {
//        return descr;
//    }
//
//    public void setDescr(String descr) {
//        this.descr = descr;
//    }
//
//    public double getBaseCost() {
//        return baseCost;
//    }
//
//    public void setBaseCost(double baseCost) {
//        this.baseCost = baseCost;
//    }
}
