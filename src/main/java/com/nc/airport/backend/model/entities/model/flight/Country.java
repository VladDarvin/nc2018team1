package com.nc.airport.backend.model.entities.model.flight;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ObjectType(ID = "1")
@Getter
@Setter
@ToString(callSuper = true)
public class Country extends BaseEntity {

    @ValueField(ID = "1")
    private String name;
}
