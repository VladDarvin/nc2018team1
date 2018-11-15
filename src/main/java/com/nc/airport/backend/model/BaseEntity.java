package com.nc.airport.backend.model;

import java.math.BigInteger;

/**
 * Base class for POJOs that are intended to be converted into mutable
 */
public abstract class BaseEntity {
    private BigInteger objectId;
    private BigInteger parentId;
    private String objectName;
    private String objectDescription;

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }
}
