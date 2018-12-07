package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ObjectType(ID = "14")
public class Passport extends BaseEntity {

    @ValueField(ID = "41")
    private String sn;

    @ValueField(ID = "42")
    private String country;

    @DateField(ID = "43")
    private LocalDateTime birthDate;
}
