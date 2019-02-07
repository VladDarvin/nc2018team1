package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.service.PrintableTicketService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@CrossOrigin
public class PrintableTicketController {
    PrintableTicketService printableTicketService;

    PrintableTicketController(PrintableTicketService service) {
        printableTicketService = service;
    }

    @GetMapping("/tickets/send/recipientEmail={email}")
    public boolean sendTicketPdfToEmail(@PathVariable(name = "email") String recipientEmail,
                                        @RequestBody Ticket ticket) {

         return printableTicketService.sendTicketByEmail(ticket, recipientEmail);
    }

    @GetMapping("/tickets/lost/onFlight={flightId}/passport={serialNumber}")
    public Ticket replaceLostTicket(@PathVariable(name = "flightId") BigInteger flightId,
                                    @PathVariable(name = "serialNumber") String passportSerialNumber) {

        return printableTicketService.findTicketByFlightAndPassport(flightId, passportSerialNumber);
    }
}
