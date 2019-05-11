package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PassportController {
    private PassportService passportService;

    @Autowired
    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @RequestMapping(value = "/passports/objectId={objectId}", method = RequestMethod.GET)
    public Passport getPassportById(@PathVariable BigInteger objectId) {
        return passportService.findPassportByReference(objectId);
    }
}
