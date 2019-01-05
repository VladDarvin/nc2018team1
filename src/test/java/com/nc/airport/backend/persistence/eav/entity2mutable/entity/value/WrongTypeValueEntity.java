package com.nc.airport.backend.persistence.eav.entity2mutable.entity.value;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;

import java.util.Objects;

public class WrongTypeValueEntity extends ValidNoFieldsEntity {
    @ValueField(ID = "1")
    protected Airline entity = new Airline();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WrongTypeValueEntity that = (WrongTypeValueEntity) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entity);
    }
}
