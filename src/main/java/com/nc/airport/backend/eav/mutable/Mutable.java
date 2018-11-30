package com.nc.airport.backend.eav.mutable;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * An object that is used as a bridge between pojo models and EAV-database schema
 * It contains all the information needed either to create the object instance
 * in database, or to create a pojo instance from the database records.
 */
@Getter
@Setter
public class Mutable {
    /**
     * Objects.Object_id
     */
    private BigInteger objectId;

    /**
     * ObjType.Object_type_id
     */
    private BigInteger objectTypeId;

    /**
     * ObjType.Parent_id
     */
    private BigInteger parentId;

    /**
     * Objects.Name
     */
    private String objectName;

    /**
     * Objects.Description
     */
    private String objectDescription;

    /**
     * BigInt - Attributes.Attr_id
     * String - Attributes.Value
     */
    private Map<BigInteger, String> values;

    /**
     * BigInt - Attributes.Attr_id
     * LocalDate - Attributes.Date_value
     */
    private Map<BigInteger, LocalDateTime> dateValues;

    /**
     * BigInt - Attributes.Attr_id
     * List - Attributes.List_value_id
     */
    private Map<BigInteger, BigInteger> listValues;

    /**
     * BigInt - ObjReference.Attr_id
     * BigInt - ObjReference.Reference
     */
    private Map<BigInteger, BigInteger> references;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mutable mutable = (Mutable) o;
        return Objects.equals(objectId, mutable.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
