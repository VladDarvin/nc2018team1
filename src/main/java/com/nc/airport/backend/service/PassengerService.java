package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class PassengerService extends AbstractService<Passenger> {

    private UserService userService;

    public PassengerService(EavCrudRepository<Passenger> repository, UserService userService) {
        super(Passenger.class, repository);
        this.userService = userService;
    }


//    public List<Passenger> findAllPassengers() {
////         TODO USE FINDSLICE
//        return passengerRepository.findAll(Passenger.class);
//    }

//    public List<Passenger> getTenPassengers(int page) {
//        int offset = (page - 1) * 10;
//        return passengerRepository.findSlice(Passenger.class, 1 + offset, 10 + offset);
//    }

    public List<Passenger> findAllPassengersByUserId(String userLogin, int page) {
        User parentUser = userService.findByLogin(userLogin);
        return repository.findSliceOfChildren(parentUser.getObjectId(), Passenger.class, new Page(page - 1));
    }

//    public Passenger addPassenger(Passenger passenger) {
//        return passengerRepository.save(passenger);
//    }
//
//    public void deletePassenger(BigInteger id) {
//        passengerRepository.deleteById(id);
//    }
//
//    public BigInteger getPassengersAmount() {
//        return passengerRepository.count(Passenger.class);
//    }

}
