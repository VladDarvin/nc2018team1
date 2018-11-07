package com.nc.airport.backend.eav.filtering;

import java.util.Set;

public class FilterEntity {
    String type;
    Set<Object> values;

    public FilterEntity(String type, Set<Object> values) {
        this.type = type;
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
