package com.nc.airport.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Base class for POJOs that are intended to be converted into mutable
 */
@Getter
@Setter
public abstract class BaseEntity {
    private BigInteger objectId;
    private BigInteger parentId;
    private String objectName;
    private String objectDescription;
}
