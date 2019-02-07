package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.dto.TicketDTO;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.service.PassengerService;
import com.nc.airport.backend.service.PassportService;
import com.nc.airport.backend.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {
    private PassengerService passengerService;
    private PassportService passportService;
    private TicketService ticketService;

    public TicketController(TicketService ticketService, PassengerService passengerService, PassportService passportService) {
        this.ticketService = ticketService;
        this.passengerService = passengerService;
        this.passportService = passportService;
    }

    @GetMapping("/tickets/count/search={searchString}")
    public BigInteger getCountOfTicketsByFilter(@PathVariable(name = "searchString") String searchString) {
        return ticketService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/tickets/search/page={page}")
    public ResponseFilteringWrapper searchTickets(@PathVariable(name = "page") int page,
                                                  @RequestBody SortingFilteringWrapper wrapper) {
        return ticketService.getTicketsInfo(wrapper.getSearchString(), wrapper.getFilters(), page);
    }

    @PutMapping("/tickets")
    public TicketDTO editTicketsInfo(@RequestBody TicketDTO ticketDTO) {
        Passport passport = ticketDTO.getPassport();
        passport = passportService.updateEntity(passport);
        Passenger passenger = ticketDTO.getPassenger();
        passenger.setPassportId(passport.getObjectId());
        passenger = passengerService.updateEntity(passenger);
        Ticket ticket = ticketDTO.getTicket();
        ticket = (Ticket) ticketService.updateEntity(ticket);
        ticketDTO.setPassport(passport);
        ticketDTO.setPassenger(passenger);
        ticketDTO.setTicket(ticket);
        return ticketDTO;
    }

    @PutMapping("/tickets/book")
    public List<Ticket> saveTickets(@RequestBody List<Ticket> tickets) {
        return ticketService.saveAll(tickets);
    }
}
