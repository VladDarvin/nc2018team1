package com.nc.airport.backend.model.entities.model.airplane;

public class Seat {

    private int id;
    private int airplaneId;
    private int seatTypeId;
    private int row;
    private int col;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }

    public int getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(int seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
