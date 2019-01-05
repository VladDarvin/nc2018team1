package com.nc.airport.backend.persistence.eav.entity2mutable.entity.datetime;

import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@ObjectType(ID = "1")
@ToString
public class ValidDateTimeEntity extends ValidNoFieldsEntity {
    @DateField(ID = "1")
    protected LocalDateTime ldt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ValidDateTimeEntity that = (ValidDateTimeEntity) o;
        return Objects.equals(ldt, that.ldt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ldt);
    }
}
