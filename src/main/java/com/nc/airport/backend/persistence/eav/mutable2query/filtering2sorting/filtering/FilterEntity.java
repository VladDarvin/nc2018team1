package com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering;

import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.PropertiesEntityType;

import java.math.BigInteger;
import java.util.Set;

public class FilterEntity {
    private String type;
    private Set<Object> values;

    /*filter by property*/
    public FilterEntity(PropertiesEntityType type, Set<Object> values) {
        this.type = "O." + type.getType();
        this.values = values;
    }

    /*filter by field*/
    public FilterEntity(BigInteger attrId, Set<Object> values) {
        this.type = "ATTR" + attrId;
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Object> getValues() {
        return values;
    }

    public void setValues(Set<Object> values) {
        this.values = values;
    }
}
