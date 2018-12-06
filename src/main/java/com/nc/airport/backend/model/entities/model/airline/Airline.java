package com.nc.airport.backend.model.entities.model.airline;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "4")
public class Airline {

    @ValueField(ID = "12")
    private String name;

    @ValueField(ID = "13")
    private String descr;

    @ValueField(ID = "14")
    private String phone;

    @ValueField(ID = "15")
    private String email;

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
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
