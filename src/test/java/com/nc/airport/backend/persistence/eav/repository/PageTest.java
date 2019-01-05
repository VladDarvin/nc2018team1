package com.nc.airport.backend.persistence.eav.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PageTest {
    @Test
    public void givenSize15AndPage0_testFirstAndLastRows() {
        Page page = new Page(15, 0);

        assertEquals(1, page.getFirstRow());
        assertEquals(15, page.getLastRow());
    }

    @Test
    public void givenSize10AndPage1_testFirstAndLastRows() {
        Page page = new Page(1);

        assertEquals(11, page.getFirstRow());
        assertEquals(20, page.getLastRow());
    }
}
