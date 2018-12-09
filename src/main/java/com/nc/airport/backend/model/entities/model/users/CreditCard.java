package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@ObjectType(ID = "16")
@Getter
@Setter
@ToString(callSuper = true)
public class CreditCard extends BaseEntity {

    @ValueField(ID = "50")
    private String number;

    @ValueField(ID = "51")
    private String month;

    @ValueField(ID = "52")
    private String year;

    @ValueField(ID = "53")
    private String cvv;

    @ValueField(ID = "54")
    private String nickname;

    @ReferenceField(ID = "55")
    private BigInteger authorizedUserId;
}
