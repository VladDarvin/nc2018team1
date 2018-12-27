package com.nc.airport.backend.persistence.eav.entity2mutable.entity.enumfield;

import com.nc.airport.backend.persistence.eav.annotations.enums.ListValue;

public enum TestType {
    @ListValue(ID = "1")
    STATE1,
    @ListValue(ID = "2")
    STATE2,
    @ListValue(ID = "3")
    STATE3
}
