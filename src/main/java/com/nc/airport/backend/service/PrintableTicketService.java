package com.nc.airport.backend.service;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Ticket;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConsistencyException;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.model.dto.PrintableTicket;
import com.nc.airport.backend.util.mail.TicketSender;
import com.nc.airport.backend.util.print.pdf.PdfTicketGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class PrintableTicketService {
    private EavCrudRepository repository;

    @Autowired
    public PrintableTicketService(EavCrudRepository repository) {
        this.repository = repository;
        TicketSender.authenticate("ticket_service@airpost.net", "lgahaf6er3en55da");
    }

    public boolean sendTicketByEmail(Ticket ticket,
                                     String recipientEmail) {

        PrintableTicket printableTicket;
        ByteArrayOutputStream outputStream;
        byte[] ticketPdf;

        printableTicket = getPrintableTicket(ticket);
        outputStream = new ByteArrayOutputStream();
        PdfTicketGenerator.putTicketPdfInOutputStream(printableTicket, outputStream);
        ticketPdf = outputStream.toByteArray();

        return TicketSender.sendTicketById(ticketPdf, recipientEmail);
    }

    public PrintableTicket getPrintableTicket(Ticket ticket) {

        PrintableTicket printableTicket = new PrintableTicket();
        Flight flight;
        Airport departureAirport;
        Airport arrivalAirport;
        Airplane airplane;
        Airline airline;
        Seat seat;
        SeatType seatType;
        Passenger passenger;

        flight = getEntityById(ticket.getFlightId(), Flight.class);
        departureAirport = getEntityById(flight.getDepartureAirportId(), Airport.class);
        arrivalAirport = getEntityById(flight.getArrivalAirportId(), Airport.class);
        airplane = getEntityById(flight.getAirplaneId(), Airplane.class);
        airline = getEntityById(airplane.getAirlineId(), Airline.class);
        seat = getEntityById(ticket.getSeatId(), Seat.class);
        seatType = getEntityById(seat.getSeatTypeId(), SeatType.class);
        passenger = getEntityById(ticket.getPassengerId(), Passenger.class);

        printableTicket.setPassengerFirstName(passenger.getFirstName());
        printableTicket.setPassengerLastName(passenger.getLastName());
        printableTicket.setSeatType(seatType.getName());
        printableTicket.setSeatColumn(seat.getCol());
        printableTicket.setSeatRow(seat.getRow());
        printableTicket.setFlightNumber(flight.getFlightNumber());
        printableTicket.setExpectedDepartureDatetime(flight.getExpectedDepartureDatetime());
        printableTicket.setDepartureCity(departureAirport.getCity());
        printableTicket.setExpectedArrivalDatetime(flight.getExpectedArrivalDatetime());
        printableTicket.setArrivalCity(arrivalAirport.getCity());
        printableTicket.setAirlineName(airline.getName());
        printableTicket.setAirlineEmail(airline.getEmail());
        printableTicket.setAirlinePhoneNumber(airline.getPhoneNumber());

        return printableTicket;
    }

    @SuppressWarnings("unchecked")
    private <T> T getEntityById(BigInteger objectId, Class<T> entityClass) {
        Optional<BaseEntity> entityWrapper = repository.findById(objectId, entityClass);
        return (T) entityWrapper.
                orElseThrow(() -> new DatabaseConsistencyException("Invalid database instance: Ticket missing "
                        + entityClass.getSimpleName()));
    }

    private double calculateCost(Flight flight, Seat seat, SeatType seatType) {
        return flight.getBaseCost().doubleValue() + seat.getModifier() + seatType.getModifier();
    }
}
