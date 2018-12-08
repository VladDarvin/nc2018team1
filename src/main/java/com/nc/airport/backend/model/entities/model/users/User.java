package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@ObjectType(ID = "15")
public class User extends BaseEntity {

    @ValueField(ID = "44")
    private String login;

    @ValueField(ID = "45")
    private String password;

    @ValueField(ID = "46")
    private String email;

    @ValueField(ID = "47")
    private String phone;

    @ValueField(ID = "48")
    private String nickname;

    @ReferenceField(ID = "49")
    private BigInteger userRoleId;
}
