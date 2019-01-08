package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PassengerController {
    private PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @RequestMapping(value = "/passengers/userLogin={userLogin}/page={page}", method = RequestMethod.GET)
    public List<Passenger> getAllPassengers(@PathVariable String userLogin, @PathVariable int page) {
        System.err.println(userLogin);
        return passengerService.findAllPassengersByUserId(userLogin, page);
    }

//    @RequestMapping(value = "/passengers", method = RequestMethod.POST)
//    public Passenger addNewPassenger(@RequestBody Passenger passenger) {
//        return passengerService.addPassenger(passenger);
//    }
//
//    @PutMapping(value = "/passengers")
//    public Passenger editPassenger(@RequestBody Passenger passenger) {
//        return passengerService.addPassenger(passenger);
//    }
//
//    @DeleteMapping(value = "/passengers/{id}")
//    public void deletePassenger(@PathVariable(name = "id") BigInteger id) {
//        passengerService.deletePassenger(id);
//    }
//
//    @GetMapping(value = "/passengers/count")
//    public BigInteger getCountOfPassengers() {
//        return passengerService.getPassengersAmount();
//    }

//    @RequestMapping(value = "/passengers/page={page}", method = RequestMethod.GET)
//    public List<Passenger> getTenPassengers(@PathVariable(name = "page") int page) {
//        return passengerService.getTenPassengers(page);
//    }

}
