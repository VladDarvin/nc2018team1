package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "1")
public class Country extends BaseEntity {

    @ValueField(ID = "1")
    private String name;
}
