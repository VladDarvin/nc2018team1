package com.nc.airport.backend.model.entities.model.airplane.dto;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString(callSuper = true)
public class SeatDto extends BaseEntity {
    private Integer col;
    private Integer row;
    private AirplaneDto airplane;
    private SeatType seatType;
    private Double modifier;

    public SeatDto() {
    }

    /**
     * Creates Airplane dto only with ids, anything else like airplane name or seatType name you should add on your own
     * @param seat
     */
    public SeatDto(Seat seat) {
        super(seat);
        this.col = seat.getCol();
        this.row = seat.getRow();
        this.airplane = new AirplaneDto(seat.getAirplaneId());
        this.seatType = new SeatType(seat.getSeatTypeId());
        this.modifier = seat.getModifier();
    }

    public SeatDto(BigInteger objectId) {
        super(objectId);
    }
}
