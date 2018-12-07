package com.nc.airport.backend.eav.filtering;

public enum PropertiesEntityType implements EntityType {
    OBJECT_ID("OBJECT_ID"),
    PARENT_ID("PARENT_ID"),
    OBJECT_TYPE_ID("OBJECT_TYPE_ID"),
    NAME("NAME"),
    DESCRIPTION("DESCRIPTION");

    private String typeString;

    PropertiesEntityType(String typeString) {
        this.typeString = typeString;
    }

    public String getType() {
        return typeString;
    }
}
