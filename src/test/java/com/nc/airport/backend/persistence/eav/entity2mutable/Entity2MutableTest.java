package com.nc.airport.backend.persistence.eav.entity2mutable;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.Mutable;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
public class Entity2MutableTest {
    @Autowired
    private Entity2Mutable e2m;
    private Airline initialAirline;

    @Before
    public void instantiateValidAirline() {
        initialAirline = new Airline();
        initialAirline.setName("TestAirline");
        initialAirline.setDescr("TestDescr");
        initialAirline.setEmail("TestEmail");
        initialAirline.setPhone("TestPhone");
        log.info("Created entity : " + initialAirline);
    }

    @Test
    public void givenAirlineFilled_whenConverted2MutAndBack_thenEqual() {
        Mutable mutable = e2m.convertEntityToMutable(initialAirline);
        log.info("Converted mutable : " + mutable);

        Airline convertedAirline = e2m.convertMutableToEntity(mutable, Airline.class);
        log.info("Converted entity : " + convertedAirline);

        Assert.assertEquals(initialAirline, convertedAirline);
    }

    //TODO test datevalue, listvalue, referencevalue
}
