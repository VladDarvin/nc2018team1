package com.nc.airport.backend.model.POJOs;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

@ObjectType(ID = "")
public class Aircompany {

    @Attribute(ID = "")
    @Value
    private String name;

    @Attribute(ID = "")
    @Value
    private String description;

    @Attribute(ID = "")
    @Value
    private String country;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
