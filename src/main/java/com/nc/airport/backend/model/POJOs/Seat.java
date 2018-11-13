package com.nc.airport.backend.model.POJOs;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.List;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

@ObjectType(ID = "")
public class Seat {

    @Attribute(ID = "")
    @Value
    private int id;

    @Attribute(ID = "")
    @List
    private SeatClass seatClass;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }



    enum SeatClass {
        Economy,
        Economy_Plus,
        Business,
        First
    }
}
