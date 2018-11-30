package com.nc.airport.backend.model.entities.model.ticketinfo;

public class TicketHasExtra {

    private int ticketId;
    private int extraId;
    private int quantity;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getExtraId() {
        return extraId;
    }

    public void setExtraId(int extraId) {
        this.extraId = extraId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
