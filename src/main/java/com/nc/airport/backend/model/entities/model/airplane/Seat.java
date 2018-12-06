package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "8")
public class Seat {

    @ReferenceField(ID = "23")
    private int airplaneId;

    @ReferenceField(ID = "24")
    private int seatTypeId;

    @ValueField(ID = "25")
    private int row;

    @ValueField(ID = "26")
    private int col;

//    public int getAirplaneId() {
//        return airplaneId;
//    }
//
//    public void setAirplaneId(int airplaneId) {
//        this.airplaneId = airplaneId;
//    }
//
//    public int getSeatTypeId() {
//        return seatTypeId;
//    }
//
//    public void setSeatTypeId(int seatTypeId) {
//        this.seatTypeId = seatTypeId;
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public int getCol() {
//        return col;
//    }
//
//    public void setCol(int col) {
//        this.col = col;
//    }
}
