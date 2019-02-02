package com.nc.airport.backend.model;

import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
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

    public BaseEntity() {}

    public BaseEntity(BaseEntity entity) {
        this.objectId = entity.getObjectId();
        this.parentId = entity.getParentId();
        this.objectName = entity.getObjectName();
        this.objectDescription = entity.getObjectDescription();
    }

    public BaseEntity(BigInteger objectId) {
        this.objectId = objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(objectId, that.objectId) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(ReflectionHelper.getObjTypeId(this.getClass()),
                        ReflectionHelper.getObjTypeId(that.getClass()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
