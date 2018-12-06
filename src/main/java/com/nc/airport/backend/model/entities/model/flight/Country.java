package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "1")
public class Country extends BaseEntity {

    @ValueField(ID = "1")
    private String name;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
