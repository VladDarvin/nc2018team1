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
    private Map<BigInteger, Object> attributeSet;
    private Map<BigInteger, BigInteger> referenceSet;

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

    public Map<BigInteger, Object> getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(Map<BigInteger, Object> attributeSet) {
        this.attributeSet = attributeSet;
    }

    public Map<BigInteger, BigInteger> getReferenceSet() {
        return referenceSet;
    }

    public void setReferenceSet(Map<BigInteger, BigInteger> referenceSet) {
        this.referenceSet = referenceSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mutable mutable = (Mutable) o;
        return Objects.equals(objectId,      mutable.objectId) &&
                Objects.equals(objectTypeId, mutable.objectTypeId) &&
                Objects.equals(parentId,     mutable.parentId) &&
                Objects.equals(attributeSet, mutable.attributeSet) &&
                Objects.equals(referenceSet, mutable.referenceSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, objectTypeId);
    }
}
