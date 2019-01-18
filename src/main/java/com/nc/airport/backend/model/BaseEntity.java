package com.nc.airport.backend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Base class for POJOs that are intended to be converted into mutable
 */
@Setter
@Getter
@ToString
public abstract class BaseEntity {
    private BigInteger objectId;
    private BigInteger parentId;
    private String objectName;
    private String objectDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(objectId, that.objectId) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, parentId);
    }
}
