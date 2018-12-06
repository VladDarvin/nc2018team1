package com.nc.airport.backend.model.entities.model.ticketinfo;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "11")
public class TicketHasExtra {

    @ReferenceField(ID = "34")
    private int ticketId;

    @ReferenceField(ID = "35")
    private int extraId;

    @ValueField(ID = "36")
    private int quantity;

//    public int getTicketId() {
//        return ticketId;
//    }
//
//    public void setTicketId(int ticketId) {
//        this.ticketId = ticketId;
//    }
//
//    public int getExtraId() {
//        return extraId;
//    }
//
//    public void setExtraId(int extraId) {
//        this.extraId = extraId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
}
