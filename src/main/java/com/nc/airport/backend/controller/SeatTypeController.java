package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.service.SeatTypeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seat-type")
public class SeatTypeController {

    private SeatTypeService seatTypeService;

    public SeatTypeController(SeatTypeService seatTypeService) {
        this.seatTypeService = seatTypeService;
    }

    @GetMapping("/objectId={objectId}")
    public SeatType getSeatTypeById(@PathVariable BigInteger objectId) {
        return seatTypeService.getSeatTypeById(objectId).get();
    }

    @GetMapping
    public List<SeatType> getAllSeatTypes() {
        return seatTypeService.findAllSeatTypes();
    }

    @GetMapping("/page={page}")
    public List<SeatType> getTenSeatTypes(@PathVariable(name = "page") int page) {
        return seatTypeService.getTenEntities(page);
    }

    @PostMapping
    public SeatType addNewSeatType(@RequestBody SeatType entity) {
        return seatTypeService.updateEntity(entity);
    }

    @PutMapping
    public SeatType editSeatType(@RequestBody SeatType entity) {
        return seatTypeService.updateEntity(entity);
    }

    @DeleteMapping("/{id}")
    public void deleteSeatType(@PathVariable(name = "id") BigInteger id) {
        seatTypeService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfSeatTypes() {
        return seatTypeService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfSeatTypesByFilter(@PathVariable(name = "searchString") String searchString) {
        return seatTypeService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchSeatTypes(@PathVariable(name = "page") int page,
                                                @RequestBody SortingFilteringWrapper wrapper) {
        return seatTypeService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
