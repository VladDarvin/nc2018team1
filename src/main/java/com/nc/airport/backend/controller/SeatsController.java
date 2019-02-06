package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.dto.SeatDto;
import com.nc.airport.backend.service.HybridFlightService;
import com.nc.airport.backend.service.SeatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seats")
@Log4j2
public class SeatsController {

    HybridFlightService hybridService;
    SeatService seatService;

    public SeatsController(HybridFlightService flightService, SeatService seatService) {
        this.hybridService = flightService;
        this.seatService = seatService;
    }

    @PostMapping
    public Seat addNewSeat(@RequestBody Seat seat) {
        return seatService.insertEntity(seat);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable(name = "id") BigInteger id) {
        seatService.deleteEntity(id);
    }

    @PutMapping
    public Seat editSeat(@RequestBody Seat seat) {
        return seatService.updateEntity(seat);
    }

    @GetMapping("/count")
    public BigInteger getCountOfSeats() {
        return seatService.getEntitiesAmount();
    }

    @GetMapping("/page={page}")
    public List<Seat> getTenSeats(@PathVariable(name = "page") int page) {
        return seatService.getTenEntities(page);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchAirlines(@PathVariable(name = "page") int page,
                                                   @RequestBody SortingFilteringWrapper wrapper) {
        return seatService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

    @GetMapping
    public List<Seat> getAll() {
        return seatService.getAll();
    }

    @GetMapping("/flightId={objectId}")
    public List<SeatDto> getByFlightId(@PathVariable String objectId) {
        List<SeatDto> seats = hybridService.getSeatsByFlightObjId(new BigInteger(objectId));
        log.debug(seats);
        return seats;
    }

    @GetMapping("/airplaneId={objectId}")
    public List<SeatDto> getByPlaneId(@PathVariable BigInteger objectId) {
        List<SeatDto> seats = seatService.getByPlaneId(objectId);
        log.debug(seats);
        return seats;
    }

    @PostMapping("/airplaneId={id}")
    public List<SeatDto> saveAll(@RequestBody List<SeatDto> seats, @PathVariable(name = "id") BigInteger id) {
        List<SeatDto> updatedSeats = seatService.saveAll(seats, id);
        log.debug("updated seats: " + updatedSeats);
        return updatedSeats;
    }
}
