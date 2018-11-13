package com.nc.airport.backend.model.POJOs;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Reference;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

import java.util.List;

@ObjectType(ID = "")
public class Plane {

    @Attribute(ID = "")
    @Value
    private String brand;

    @Attribute(ID = "")
    @Value
    private String model;

    @Attribute(ID = "")
//  @ListValue
    private Type type;

    @Attribute(ID = "")
    @Reference
    private Aircompany aircompany;

    @Attribute(ID = "")
    @Reference
    private List<Seat> seats;



    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Aircompany getAircompany() {
        return aircompany;
    }

    public void setAircompany(Aircompany aircompany) {
        this.aircompany = aircompany;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }


    enum Type {
        Big,
        Medium,
        Small
    }
}
