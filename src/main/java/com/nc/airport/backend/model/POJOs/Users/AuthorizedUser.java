package com.nc.airport.backend.model.POJOs.Users;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

@ObjectType(ID = "2")
public class AuthorizedUser extends AbstractUser {

    @Attribute(ID = "3")
    @Value
    private int id;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
