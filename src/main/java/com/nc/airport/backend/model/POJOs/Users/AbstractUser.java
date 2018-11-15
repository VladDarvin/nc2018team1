package com.nc.airport.backend.model.POJOs.Users;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.attribute.value.Value;

abstract class AbstractUser {

    @Attribute(ID = "1")
    @Value
    private String firstName;

    @Attribute(ID = "2")
    @Value
    private String lastName;

    AbstractUser(String firstname, String lastName) {
        this.firstName = firstname;
        this.lastName = lastName;
    }

    AbstractUser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
