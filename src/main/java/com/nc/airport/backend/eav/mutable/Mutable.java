package com.nc.airport.backend.eav.mutable;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * An object that is used as a bridge between pojo models and EAV-database schema
 * It contains all the information needed either to create the object instance
 * in database, or to create a pojo instance from the database records.
 */
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

    public Map<BigInteger, BigInteger> getReferences() {
        return references;
    }

    public void setReferences(Map<BigInteger, BigInteger> references) {
        this.references = references;
    }

    public Map<BigInteger, String> getValues() {
        return values;
    }

    public void setValues(Map<BigInteger, String> values) {
        this.values = values;
    }

    public Map<BigInteger, LocalDateTime> getDateValues() {
        return dateValues;
    }

    public void setDateValues(Map<BigInteger, LocalDateTime> dateValues) {
        this.dateValues = dateValues;
    }

    public Map<BigInteger, BigInteger> getListValues() {
        return listValues;
    }

    public void setListValues(Map<BigInteger, BigInteger> listValues) {
        this.listValues = listValues;
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
