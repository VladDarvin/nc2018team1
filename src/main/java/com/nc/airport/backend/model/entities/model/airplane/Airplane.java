package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airplane.dto.AirplaneDto;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "5")
@Getter
@Setter
@ToString(callSuper = true)
public class Airplane extends BaseEntity {

    @ValueField(ID = "16")
    private String model;

    @ReferenceField(ID = "17")
    private BigInteger airlineId;

    public Airplane() {
    }

    public Airplane(AirplaneDto airplaneDto) {
        super(airplaneDto);
        this.model = airplaneDto.getModel();
        this.airlineId = airplaneDto.getAirline().getObjectId();
    }
}
