package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "2")
public class Airport extends BaseEntity {

    @ValueField(ID = "2")
    private String name;

    @ReferenceField(ID = "3")
    private int countryId;

    @ValueField(ID = "4")
    private String address;

    @ValueField(ID = "5")
    private String city;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getCountryId() {
//        return countryId;
//    }
//
//    public void setCountryId(int countryId) {
//        this.countryId = countryId;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
}
