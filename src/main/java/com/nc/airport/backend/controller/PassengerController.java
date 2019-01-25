package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.PassengerPassportDTO;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.PassengerService;
import com.nc.airport.backend.service.PassportService;
import com.nc.airport.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PassengerController {
    private PassengerService passengerService;
    private PassportService passportService;
    private UserService userService;

    @Autowired
    public PassengerController(PassengerService passengerService, PassportService passportService,
                               UserService userService) {
        this.passengerService = passengerService;
        this.passportService = passportService;
        this.userService = userService;
    }

    @RequestMapping(value = "/passengers/userLogin={userLogin}/page={page}", method = RequestMethod.GET)
    public List<Passenger> getAllPassengers(@PathVariable String userLogin, @PathVariable int page) {
        return passengerService.findAllPassengersByUserLogin(userLogin, page);
    }

    @PostMapping(value = "/passengers/userLogin={userLogin}")
    public PassengerPassportDTO addNewPassenger(@PathVariable String userLogin, @RequestBody PassengerPassportDTO dto) {
        Passport passport = dto.getPassport();
        passport = passportService.updateEntity(passport);
        User user = userService.findByLogin(userLogin);
        Passenger passenger = dto.getPassenger();
        passenger.setPassportId(passport.getObjectId());
        passenger.setParentId(user.getObjectId());
        passenger = passengerService.updateEntity(passenger);
        return new PassengerPassportDTO(passenger, passport);
    }

    @PostMapping(value = "/passengers/userLogin={userLogin}/search/page={page}")
    public ResponseFilteringWrapper searchPassengers(@PathVariable(name = "userLogin") String userLogin, @PathVariable(name = "page") int page,
                                                     @RequestBody SortingFilteringWrapper wrapper) {
        return passengerService.filterAndSortEntitiesByUserLogin(userLogin, page, wrapper.getSearchString(), wrapper.getSortList());
    }

    @DeleteMapping(value = "/passengers/passenger={psgId}/passport={pstId}")
    public void deletePassenger(@PathVariable(name = "psgId") BigInteger psgId, @PathVariable(name = "pstId") BigInteger pstId) {
        passportService.deleteEntity(pstId);
        passengerService.deleteEntity(psgId);
    }

}
