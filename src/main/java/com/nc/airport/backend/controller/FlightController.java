package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.FlightDTO;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Flight;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.FlightService;
import com.nc.airport.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {
    private FlightService flightService;
    private UserService userService;

    @Autowired
    public FlightController(FlightService flightService, UserService userService) {
        this.flightService = flightService;
        this.userService = userService;
    }

    @RequestMapping(value = "/user-flights/userLogin={userLogin}/page={page}", method = RequestMethod.GET)
    public List<FlightDTO> getAllFlights(@PathVariable String userLogin, @PathVariable int page) {
        User parentUser = userService.findByLogin(userLogin);
        return flightService.findAllFlightsByUserId(parentUser.getObjectId(), page);
    }

    // --------------------------

    @GetMapping(value = "/flights/page={page}")
    public List<FlightDTO> getTenFlights(@PathVariable int page) {
        return flightService.getAllFlights(page);
    }

    @GetMapping("/flights/count")
    public BigInteger getCountOfFlights() {
        return flightService.getEntitiesAmount();
    }

    @PutMapping("/flights")
    public Flight editFlight(@RequestBody Flight flight) {
        return (Flight) flightService.updateEntity(flight);
    }

    @PostMapping("/flights")
    public Flight addNewFlight(@RequestBody Flight flight) {
        return (Flight) flightService.insertEntity(flight);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlight(@PathVariable(name = "id") BigInteger id) {
        flightService.deleteEntity(id);
    }

    @GetMapping("/flights/count/search={searchString}")
    public BigInteger getCountOfFlightsByFilter(@PathVariable(name = "searchString") String searchString) {
        return flightService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/flights/search/page={page}")
    public ResponseFilteringWrapper searchFlights(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return flightService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }

}
