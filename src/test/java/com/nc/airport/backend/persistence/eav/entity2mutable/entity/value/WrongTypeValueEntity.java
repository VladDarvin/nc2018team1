package com.nc.airport.backend.persistence.eav.entity2mutable.entity.value;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;

public class WrongTypeValueEntity extends ValidNoFieldsEntity {
    @ValueField(ID = "1")
    protected Airline entity = new Airline();
}
