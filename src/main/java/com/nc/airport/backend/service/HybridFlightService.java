package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.dto.SeatDto;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class HybridFlightService {

    private final SeatService seatService;
    private final FlightService flightService;
    private final TicketService ticketService;

    public HybridFlightService(SeatService seatService, FlightService flightService, TicketService ticketService) {
        this.seatService = seatService;
        this.flightService = flightService;
        this.ticketService = ticketService;
    }

    public List<SeatDto> getSeatsByFlightObjId(BigInteger objectId) {
        List<Ticket> ticketsOfFlight = ticketService.getAllByFlightId(objectId);
        List<Seat> seatsByTickets = new ArrayList<>();

        for (Ticket ticket : ticketsOfFlight) {
            Seat seat = seatService.getByObjectId(ticket.getSeatId());
            seatsByTickets.add(seat);
        }

        return seatService.seatsToSeatDtos(null, seatsByTickets);
    }
}
