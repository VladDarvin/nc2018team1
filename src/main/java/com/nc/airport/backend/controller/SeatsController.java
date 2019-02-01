package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
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

    SeatService service;

    public SeatsController(SeatService service) {
        this.service = service;
    }

    @PostMapping
    public Seat addNewSeat(@RequestBody Seat seat) {
        return service.insertEntity(seat);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable(name = "id") BigInteger id) {
        service.deleteEntity(id);
    }

    @PutMapping
    public Seat editSeat(@RequestBody Seat seat) {
        return service.updateEntity(seat);
    }

    @GetMapping("/count")
    public BigInteger getCountOfSeats() {
        return service.getEntitiesAmount();
    }

    @GetMapping("/page={page}")
    public List<Seat> getTenSeats(@PathVariable(name = "page") int page) {
        return service.getTenEntities(page);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchAirlines(@PathVariable(name = "page") int page,
                                                   @RequestBody SortingFilteringWrapper wrapper) {
        return service.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

    @GetMapping
    public List<Seat> getAll() {
        return service.getAll();
    }

    @GetMapping("/airplaneId={id}")
    public List<Seat> getByPlaneId(@PathVariable BigInteger id) {
        List<Seat> seats = service.getByPlaneId(id);
        log.debug(seats);
        return seats;
    }

}
