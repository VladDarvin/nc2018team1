package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;

@ObjectType(ID = "17")
public class UserRole {

    @ValueField(ID = "57")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
