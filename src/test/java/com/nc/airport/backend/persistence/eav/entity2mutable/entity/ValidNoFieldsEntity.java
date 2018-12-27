package com.nc.airport.backend.persistence.eav.entity2mutable.entity;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import lombok.ToString;

@ObjectType(ID = "1")
@ToString
//Extending this class (actually, any class that has annotations is currently unsupported(derived annotations are ignored))
public class ValidNoFieldsEntity extends BaseEntity {

}
