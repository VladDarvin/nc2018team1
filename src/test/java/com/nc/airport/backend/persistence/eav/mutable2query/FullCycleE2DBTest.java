package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.Entity2Mutable;
import junit.framework.TestCase;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class FullCycleE2DBTest extends TestCase {
    @Autowired
    DataSource dataSource;
    @Autowired
    Entity2Mutable e2m;
    @Autowired
    Mutable2Query m2q;

    @Test
    public void givenBaseEntityDerivative_whenStoredInDBandReadBackUsingCorePart_thenEqual() throws SQLException {
        Airplane initialAirplane = new Airplane();
        initialAirplane.setAirlineId(new BigInteger("3"));
        initialAirplane.setModel("SomeModel");
        log.info("initial airplane : {}", initialAirplane);

        log.info("Converting e2m");
        Mutable initialMutable = e2m.convertEntityToMutable(initialAirplane);

        log.info("inserting m2db");
        m2q.sqlInsert(initialMutable);
        log.info("initial mutable : {}", initialMutable);

        List<BigInteger> attrIds = new ArrayList<>();
        for (Map.Entry<BigInteger, String> bigIntegerStringEntry : initialMutable.getValues().entrySet()) {
            attrIds.add(bigIntegerStringEntry.getKey());
        }
        for (Map.Entry<BigInteger, LocalDateTime> bigIntegerLocalDateTimeEntry : initialMutable.getDateValues().entrySet()) {
            attrIds.add(bigIntegerLocalDateTimeEntry.getKey());
        }
        for (Map.Entry<BigInteger, BigInteger> bigIntegerBigIntegerEntry : initialMutable.getReferences().entrySet()) {
            attrIds.add(bigIntegerBigIntegerEntry.getKey());
        }
        for (Map.Entry<BigInteger, BigInteger> bigIntegerBigIntegerEntry : initialMutable.getListValues().entrySet()) {
            attrIds.add(bigIntegerBigIntegerEntry.getKey());
        }

        log.info("getting m from db");
        Mutable fetchedMutable = m2q.getSingleMutable(initialMutable.getObjectId(), attrIds);
        log.info("fetched mutable : {}", fetchedMutable);

        log.info("deleting inserted object");
        m2q.sqlDelete(fetchedMutable);

        log.info("converting m2e");
        Airplane fetchedAirplane = e2m.convertMutableToEntity(fetchedMutable, Airplane.class);
        log.info("converted entity : {}", fetchedAirplane);

        assertEquals(initialMutable, fetchedMutable);

        assertNotNull(fetchedAirplane.getObjectId());
        assertEquals(initialAirplane.getAirlineId(), fetchedAirplane.getAirlineId());
        assertEquals(initialAirplane.getModel(), fetchedAirplane.getModel());
    }

    @Test
    public void givenBaseEntityDerivative_whenStoredInDBWithUpdateAndReadBackUsingCorePart_thenEqual() throws SQLException {
        Airplane initialAirplane = new Airplane();
        initialAirplane.setAirlineId(new BigInteger("3"));
        initialAirplane.setModel("SomeModel");
        log.info("initial airplane : {}", initialAirplane);

        log.info("Converting e2m");
        Mutable initialMutable = e2m.convertEntityToMutable(initialAirplane);

        log.info("inserting m2db");
        m2q.sqlUpdate(initialMutable);
        log.info("initial mutable : {}", initialMutable);

        List<BigInteger> attrIds = new ArrayList<>();
        for (Map.Entry<BigInteger, String> bigIntegerStringEntry : initialMutable.getValues().entrySet()) {
            attrIds.add(bigIntegerStringEntry.getKey());
        }
        for (Map.Entry<BigInteger, LocalDateTime> bigIntegerLocalDateTimeEntry : initialMutable.getDateValues().entrySet()) {
            attrIds.add(bigIntegerLocalDateTimeEntry.getKey());
        }
        for (Map.Entry<BigInteger, BigInteger> bigIntegerBigIntegerEntry : initialMutable.getReferences().entrySet()) {
            attrIds.add(bigIntegerBigIntegerEntry.getKey());
        }
        for (Map.Entry<BigInteger, BigInteger> bigIntegerBigIntegerEntry : initialMutable.getListValues().entrySet()) {
            attrIds.add(bigIntegerBigIntegerEntry.getKey());
        }

        log.info("getting m from db");
        Mutable fetchedMutable = m2q.getSingleMutable(initialMutable.getObjectId(), attrIds);
        log.info("fetched mutable : {}", fetchedMutable);

        log.info("deleting inserted object");
        m2q.sqlDelete(fetchedMutable);

        log.info("converting m2e");
        Airplane fetchedAirplane = e2m.convertMutableToEntity(fetchedMutable, Airplane.class);
        log.info("converted entity : {}", fetchedAirplane);

        assertEquals(initialMutable, fetchedMutable);

        assertNotNull(fetchedAirplane.getObjectId());
        assertEquals(initialAirplane.getAirlineId(), fetchedAirplane.getAirlineId());
        assertEquals(initialAirplane.getModel(), fetchedAirplane.getModel());
    }
}
