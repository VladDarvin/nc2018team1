package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.model.BaseEntity;

@ObjectType(ID = "18")
public class TicketHistory extends BaseEntity {

    @ReferenceField(ID = "58")
    private int ticketId;

    @ReferenceField(ID = "59")
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
