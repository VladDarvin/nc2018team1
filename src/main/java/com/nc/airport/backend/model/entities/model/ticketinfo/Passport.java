package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ObjectType(ID = "14")
@Getter
@Setter
@ToString(callSuper = true)
public class Passport extends BaseEntity {

    @ValueField(ID = "41")
    private String serialNumber;

    @ValueField(ID = "42")
    private String country;

    @DateField(ID = "43")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
}
