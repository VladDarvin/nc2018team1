package com.nc.airport.backend.model.POJOs.Users;

import com.nc.airport.backend.eav.annotations.Attribute;
import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.Reference;
import com.nc.airport.backend.model.POJOs.Aircompany;

@ObjectType(ID = "6")
public class FinancialAnalyst extends AbstractUser {

    @Attribute(ID = "4")
    @Reference
    private Aircompany aircompany;



    public Aircompany getAircompany() {
        return aircompany;
    }

    public void setAircompany(Aircompany aircompany) {
        this.aircompany = aircompany;
    }
}