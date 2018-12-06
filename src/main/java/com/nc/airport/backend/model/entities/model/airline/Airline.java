package com.nc.airport.backend.model.entities.model.airline;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;

@ObjectType(ID = "4")
public class Airline extends BaseEntity {

    @ValueField(ID = "13")
    private String name;

    @ValueField(ID = "14")
    private String descr;

    @ValueField(ID = "15")
    private String phone;

    @ValueField(ID = "16")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
