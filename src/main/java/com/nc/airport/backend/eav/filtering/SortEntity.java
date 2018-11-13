package com.nc.airport.backend.eav.filtering;

public class SortEntity {

    private String type;
    private Boolean order;

    public SortEntity(String type, Boolean order) {
        this.type = type;
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOrder() {
        return order;
    }

    public void setOrder(Boolean order) {
        this.order = order;
    }
}
