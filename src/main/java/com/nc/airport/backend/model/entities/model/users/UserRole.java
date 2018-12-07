package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "17")
public class UserRole extends BaseEntity {

    @ValueField(ID = "56")
    private String userRole;
}
