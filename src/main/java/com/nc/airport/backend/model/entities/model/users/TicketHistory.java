package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "18")
public class TicketHistory {

    @ReferenceField(ID = "57")
    private int ticketId;

    @ReferenceField(ID = "58")
    private int authorizedUserId;

//    public int getTicketId() {
//        return ticketId;
//    }
//
//    public void setTicketId(int ticketId) {
//        this.ticketId = ticketId;
//    }
//
//    public int getAuthorizedUserId() {
//        return authorizedUserId;
//    }
//
//    public void setAuthorizedUserId(int authorizedUserId) {
//        this.authorizedUserId = authorizedUserId;
//    }
}
