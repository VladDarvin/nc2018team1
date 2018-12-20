package com.nc.airport.backend.persistence.eav.entity2mutable;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.builder.impl.DefaultEntityBuilder;
import com.nc.airport.backend.persistence.eav.entity2mutable.impl.DefaultEntity2Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.parser.impl.NonValidatingEntityParser;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Log4j2
public class Entity2MutableTest {
    private Entity2Mutable e2m = new DefaultEntity2Mutable(new NonValidatingEntityParser(), new DefaultEntityBuilder());
    private Airline initialAirline;

    @Before
    public void instantiateValidAirline() {
        initialAirline = new Airline();
        initialAirline.setName("TestAirline");
        initialAirline.setDescr("TestDescr");
        initialAirline.setEmail("TestEmail");
        initialAirline.setPhone("TestPhone");
        log.info("Created test entity : " + initialAirline);
    }

    @Test
    public void givenAirlineFilled_whenConverted2MutAndBack_thenEqual() {
        Mutable mutable = e2m.convertEntityToMutable(initialAirline);
        log.info("Converted test mutable : " + mutable);

        Airline convertedAirline = e2m.convertMutableToEntity(mutable, Airline.class);
        log.info("Converted test entity : " + convertedAirline);

        Assert.assertEquals(initialAirline, convertedAirline);
    }

    //TODO test datevalue, listvalue, referencevalue
}
