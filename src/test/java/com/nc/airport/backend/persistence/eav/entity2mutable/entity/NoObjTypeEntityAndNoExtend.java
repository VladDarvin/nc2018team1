package com.nc.airport.backend.persistence.eav.entity2mutable.entity;

import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

import java.util.Objects;

@ToString
public class NoObjTypeEntityAndNoExtend {
    @ValueField(ID = "123")
    protected String s = "test";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoObjTypeEntityAndNoExtend that = (NoObjTypeEntityAndNoExtend) o;
        return Objects.equals(s, that.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}
