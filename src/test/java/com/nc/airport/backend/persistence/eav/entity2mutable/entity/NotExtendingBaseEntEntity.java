package com.nc.airport.backend.persistence.eav.entity2mutable.entity;


import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.ToString;

import java.util.Objects;

@ObjectType(ID = "1")
@ToString
public class NotExtendingBaseEntEntity {
    @ValueField(ID = "1")
    protected String testString = "123";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotExtendingBaseEntEntity that = (NotExtendingBaseEntEntity) o;
        return Objects.equals(testString, that.testString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testString);
    }
}
