package com.nc.airport.backend.model;

import java.math.BigInteger;
import java.util.Map;

//TODO JAVADOC
public interface Entity {

    BigInteger getId();

    void setId(BigInteger id);

    BigInteger getParentId();

    void setParentId(BigInteger id);

    void fillAttributesFromMap(Map<BigInteger, Object> attributes);

    void fillReferencesFromMap(Map<BigInteger, BigInteger> references);
}
