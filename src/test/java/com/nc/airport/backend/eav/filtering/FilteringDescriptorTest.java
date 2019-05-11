package com.nc.airport.backend.eav.filtering;

import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilteringDescriptor;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging.PagingDescriptor;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortingDescriptor;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
public class FilteringDescriptorTest {

    @Autowired
    private FilteringDescriptor filteringDescriptor;
    @Autowired
    private SortingDescriptor sortingDescriptor;
    @Autowired
    private PagingDescriptor pagingDescriptor;

    List<FilterEntity> filterEntities;
    List<SortEntity> sortEntities;
    StringBuilder query;

    @Before
    public void instantiateEntities() {


        sortEntities = new ArrayList<>();
        sortEntities.add(new SortEntity(BigInteger.valueOf(3), true));
        sortEntities.add(new SortEntity(BigInteger.valueOf(13), false));

        query = new StringBuilder("SELECT * FROM USERS");

    }

    @Test
    public void givenStringsAndIntsAsFilters_whenFilter_thenAsExpected() {
        filterEntities = new ArrayList<>();

        Set<Object> stringValues = new HashSet<>();
        stringValues.add("mail@email.com");
        stringValues.add("email@mail.ua");
        filterEntities.add(new FilterEntity(BigInteger.valueOf(7), stringValues));

        Set<Object> intValues = new HashSet<>();
        intValues.add(new BigInteger("37"));
        intValues.add(new BigInteger("87"));
        filterEntities.add(new FilterEntity(BigInteger.valueOf(5), intValues));

        String expectedResult = " WHERE (LOWER(ATTR7) LIKE LOWER(?) OR LOWER(ATTR7) LIKE LOWER(?)) OR (ATTR5 = ? OR ATTR5 = ?)";
        String result = filteringDescriptor.doFiltering(filterEntities);

        log.info(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenStringsFilter_whenFilter_thenAsExpected() {
        filterEntities = new ArrayList<>();

        Set<Object> stringValues = new HashSet<>();
        stringValues.add("mail@email.com");
        stringValues.add("email@mail.ua");

        filterEntities.add(new FilterEntity(BigInteger.valueOf(7), stringValues));

        String expected = " WHERE (LOWER(ATTR7) LIKE LOWER(?) OR LOWER(ATTR7) LIKE LOWER(?))";
        String result = filteringDescriptor.doFiltering(filterEntities);

        log.info(result);
        assertEquals(expected, result);
    }

    @Test
    public void given3StringsFilter_whenFilter_thenAsExpected() {
        filterEntities = new ArrayList<>();

        Set<Object> stringValues = new HashSet<>();
        stringValues.add("mail@email.com");
        stringValues.add("email@mail.ua");

        filterEntities.add(new FilterEntity(BigInteger.valueOf(7), stringValues));
        filterEntities.add(new FilterEntity(BigInteger.valueOf(7), stringValues));
        filterEntities.add(new FilterEntity(BigInteger.valueOf(7), stringValues));

        String expected = " WHERE (LOWER(ATTR7) LIKE LOWER(?) OR LOWER(ATTR7) LIKE LOWER(?)) " +
                "OR (LOWER(ATTR7) LIKE LOWER(?) OR LOWER(ATTR7) LIKE LOWER(?)) " +
                "OR (LOWER(ATTR7) LIKE LOWER(?) OR LOWER(ATTR7) LIKE LOWER(?))";
        String result = filteringDescriptor.doFiltering(filterEntities);

        log.info(result);
        assertEquals(expected, result);
    }

    @Test
    public void doSorting() {
        String expectedResult = " ORDER BY ATTR3 ASC, ATTR13 DESC";
        String result = sortingDescriptor.doSorting(sortEntities);

        log.info(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void doPaging() {
        String expectedResult = "SELECT * FROM ( SELECT a.*, rownum rnum FROM (SELECT * FROM USERS) a WHERE rownum <= 10) WHERE rnum >= 1";
        String result = pagingDescriptor.getPaging(query, 1, 10);

        log.info(result);
        assertEquals(expectedResult, result);
    }
}
