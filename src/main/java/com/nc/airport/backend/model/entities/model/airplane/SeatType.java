package com.nc.airport.backend.model.entities.model.airplane;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@ObjectType(ID = "9")
@Getter
@Setter
@ToString(callSuper = true)
public class SeatType extends BaseEntity {

    @ValueField(ID = "27")
    private String name;

    @ValueField(ID = "28")
    private String description;

    @ValueField(ID = "29")
    private double baseCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SeatType seatType = (SeatType) o;
        return Double.compare(seatType.baseCost, baseCost) == 0 &&
                Objects.equals(name, seatType.name) &&
                Objects.equals(description, seatType.description);
    }
}
