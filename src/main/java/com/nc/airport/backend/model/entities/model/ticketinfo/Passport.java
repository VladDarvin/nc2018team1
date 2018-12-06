package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ObjectType(ID = "14")
public class Passport {

    @ValueField(ID = "41")
    private String sn;

    @ValueField(ID = "42")
    private String country;

    @DateField(ID = "43")
    private LocalDateTime birthDate;

//    public String getSn() {
//        return sn;
//    }
//
//    public void setSn(String sn) {
//        this.sn = sn;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public LocalDateTime getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(LocalDateTime birthDate) {
//        this.birthDate = birthDate;
//    }
}
