package com.nc.airport.backend.model.entities.model.airline;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "4")
@Getter
@Setter
@ToString(callSuper = true)
public class Airline extends BaseEntity {

    @ValueField(ID = "12")
    private String name;

    @ValueField(ID = "13")
    private String descr;

    @ValueField(ID = "14")
    private String phoneNumber;

    @ValueField(ID = "15")
    private String email;

    public Airline() {
    }

    public Airline(BigInteger objectId) {
        super(objectId);
    }
}
