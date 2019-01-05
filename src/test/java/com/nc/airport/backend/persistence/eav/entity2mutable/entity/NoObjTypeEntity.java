package com.nc.airport.backend.persistence.eav.entity2mutable.entity;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

import java.util.Objects;

@ToString
public class NoObjTypeEntity extends BaseEntity {
    @ValueField(ID = "123123")
    protected String test = "123";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NoObjTypeEntity that = (NoObjTypeEntity) o;
        return Objects.equals(test, that.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), test);
    }
}
