package com.nc.airport.backend.model.entities.model.users;

public class TicketHistory {
    
    private int ticketId;
    private int authorizedUserId;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getAuthorizedUserId() {
        return authorizedUserId;
    }

    public void setAuthorizedUserId(int authorizedUserId) {
        this.authorizedUserId = authorizedUserId;
    }
}
