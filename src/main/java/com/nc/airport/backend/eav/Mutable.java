package com.nc.airport.backend.eav;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

/**
 * An object that is used as a bridge between pojo models and EAV-database schema
 * It contains all the information needed either to create the object instance
 * in database, or to create a pojo instance from the database records.
 */
public class Mutable {
    private BigInteger objectId;
    private BigInteger objectTypeId;
    private BigInteger parentId;
    private Map<BigInteger, Object> attributes;
    private Map<BigInteger, BigInteger> references;

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public BigInteger getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(BigInteger objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public Map<BigInteger, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<BigInteger, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<BigInteger, BigInteger> getReferences() {
        return references;
    }

    public void setReferences(Map<BigInteger, BigInteger> references) {
        this.references = references;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mutable mutable = (Mutable) o;
        return Objects.equals(objectId, mutable.objectId) &&
                Objects.equals(objectTypeId, mutable.objectTypeId) &&
                Objects.equals(parentId, mutable.parentId) &&
                Objects.equals(attributes, mutable.attributes) &&
                Objects.equals(references, mutable.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, objectTypeId);
    }
}
