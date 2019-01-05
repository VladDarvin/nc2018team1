package com.nc.airport.backend.persistence.eav.entity2mutable.entity.reference;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.util.Objects;

@ObjectType(ID = "1")
@ToString
public class WrongTypeReferenceEntity extends ValidNoFieldsEntity {
    @ReferenceField(ID = "123")
    protected int i = 5;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WrongTypeReferenceEntity that = (WrongTypeReferenceEntity) o;
        return i == that.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), i);
    }
}
