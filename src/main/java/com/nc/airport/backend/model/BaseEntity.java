package com.nc.airport.backend.model;

import java.math.BigInteger;

public abstract class BaseEntity implements Entity {
    private BigInteger id;
    private BigInteger parentId;

    @Override
    public BigInteger getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }
}
