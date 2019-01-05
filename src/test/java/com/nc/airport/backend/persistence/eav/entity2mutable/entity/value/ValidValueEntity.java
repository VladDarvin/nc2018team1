package com.nc.airport.backend.persistence.eav.entity2mutable.entity.value;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.util.Objects;

@ObjectType(ID = "1")
@ToString
public class ValidValueEntity extends ValidNoFieldsEntity {
    @ValueField(ID = "123")
    protected String name = "test";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ValidValueEntity that = (ValidValueEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
