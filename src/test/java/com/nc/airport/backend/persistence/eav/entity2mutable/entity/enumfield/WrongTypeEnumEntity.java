package com.nc.airport.backend.persistence.eav.entity2mutable.entity.enumfield;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.util.Objects;

@ObjectType(ID = "1")
@ToString
public class WrongTypeEnumEntity extends ValidNoFieldsEntity {
    @ListField(ID = "123")
    protected String color = "blue";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WrongTypeEnumEntity that = (WrongTypeEnumEntity) o;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color);
    }
}
