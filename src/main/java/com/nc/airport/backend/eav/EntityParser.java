package com.nc.airport.backend.eav;

import com.nc.airport.backend.model.Entity;

import java.math.BigInteger;
import java.util.Map;

//TODO JAVADOC
public interface EntityParser<T extends Entity> {

    BigInteger parseObjectTypeId(T entity);

    Map<BigInteger, Object> parseAttributes(T entity);

    Map<BigInteger, BigInteger> parseReferences(T entity);
}
