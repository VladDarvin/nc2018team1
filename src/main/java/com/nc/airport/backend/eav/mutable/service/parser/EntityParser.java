package com.nc.airport.backend.eav.mutable.service.parser;

import com.nc.airport.backend.model.BaseEntity;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

/**
 * Used to extract data from annotated POJOs
 */
public interface EntityParser {

    /**
     * Gets id from @ObjectType annotation of a class that represents the entity
     *
     * @param entity annotated object from which Type id is read
     * @return Type id if exists, otherwise null
     */
    BigInteger parseObjectTypeId(BaseEntity entity);

    /**
     * @param entity parsed entity, fields of which have @ValueField annotations
     * @return idsToValues of fields. If field has null value it is skipped
     */
    Map<BigInteger, String> parseValues(BaseEntity entity);

    /**
     * @param entity parsed entity, fields of which have @DateField annotations
     * @return idsToValues of fields. If field has null value it is skipped
     */
    Map<BigInteger, LocalDate> parseDateValues(BaseEntity entity);

    /**
     * @param entity parsed entity, fields of which have @ListField annotations
     * @return idsToValues of fields. If field has null value it is skipped
     */
    Map<BigInteger, BigInteger> parseListValues(BaseEntity entity);

    /**
     * @param entity parsed entity, fields of which have @ReferenceField annotations
     * @return idsToValues of fields. If field has null value it is skipped
     */
    Map<BigInteger, BigInteger> parseReferences(BaseEntity entity);
}
