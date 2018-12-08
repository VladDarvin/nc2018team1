package com.nc.airport.backend.model.entities.model.airline;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "4")
public class Airline extends BaseEntity {

    @ValueField(ID = "12")
    private String name;

    @ValueField(ID = "13")
    private String descr;

    @ValueField(ID = "14")
    private String phone;

    @ValueField(ID = "15")
    private String email;
}
