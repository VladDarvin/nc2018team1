package com.nc.airport.backend.model.entities.model.airplane.dto;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SeatDto extends BaseEntity {
    private Integer col;
    private Integer row;
    private AirplaneDto airplane;
    private SeatType seatType;
    private Double modifier;
}
