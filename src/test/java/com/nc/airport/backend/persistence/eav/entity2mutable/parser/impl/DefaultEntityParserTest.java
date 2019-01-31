package com.nc.airport.backend.persistence.eav.entity2mutable.parser.impl;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.ExtraType;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
@Ignore
public class DefaultEntityParserTest {

    @Autowired
    EavCrudRepository repository;

    @Test
    public void generateObjectNameForAllObjects() {
        getAndUpdate(10, Airline.class);
        getAndUpdate(11, Airline.class);
        getAndUpdate(12, Airline.class);
        getAndUpdate(13, Airline.class);
        getAndUpdate(14, Airline.class);
        getAndUpdate(15, Airline.class);
        getAndUpdate(16, Airline.class);
        getAndUpdate(17, Country.class);
        getAndUpdate(18, Country.class);
        getAndUpdate(19, Airline.class);

        getAndUpdate(20, User.class);
        getAndUpdate(21, User.class);
        getAndUpdate(22, User.class);
        getAndUpdate(23, User.class);
        getAndUpdate(24, User.class);

        getAndUpdate(25, Passenger.class);
        getAndUpdate(26, Passport.class);

        getAndUpdate(27, Passenger.class);
        getAndUpdate(28, Passenger.class);
        getAndUpdate(29, Airplane.class);

        getAndUpdate(30, Passport.class);

        getAndUpdate(31, SeatType.class);
        getAndUpdate(32, SeatType.class);

        getAndUpdate(33, Passport.class);

        getAndUpdate(34, SeatType.class);

        getAndUpdate(35, ExtraType.class);

        getAndUpdate(36, SeatType.class);
        getAndUpdate(37, SeatType.class);

        getAndUpdate(38, Passport.class);
        getAndUpdate(39, Passenger.class);

        getAndUpdate(3, Country.class);

        getAndUpdate(40, Passport.class);
        getAndUpdate(41, Passport.class);

        getAndUpdate(4, Airline.class);
        getAndUpdate(5, Airline.class);
        getAndUpdate(6, Airline.class);
        getAndUpdate(7, Airline.class);
        getAndUpdate(8, Airline.class);
        getAndUpdate(9, Airline.class);
    }

    private <T extends BaseEntity> void getAndUpdate(int id, Class<T> clazz) {
        log.info("id: " + id + ";class: " + clazz.getSimpleName());
        Optional<T> entity = repository.findById(BigInteger.valueOf(id), clazz);
        entity.ifPresent(res -> {
            log.info("got entity: " + res);
            BaseEntity updEnt = repository.update(res);
            log.info("updated entity: " + updEnt);
        });
        if (!entity.isPresent())
            log.info("have not found entity");

        log.info("-----");
    }
}
