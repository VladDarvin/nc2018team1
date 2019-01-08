package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;
import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    @ListValue(ID = "1")
    ROLE_ADMIN,

    @ListValue(ID = "2")
    ROLE_USER,

    @ListValue(ID = "3")
    ROLE_CASHIER,

    @ListValue(ID = "4")
    ROLE_ANALYST,

    @ListValue(ID = "5")
    ROLE_CONTROLLER;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
