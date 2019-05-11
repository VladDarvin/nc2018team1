package com.nc.airport.backend.persistence.eav.entity2mutable.util;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.annotations.ObjectType;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.DateField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ListField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.persistence.eav.annotations.attribute.value.ValueField;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReflectionHelperTest {

    @Test
    public void givenBaseEntityChild_whenComparedActualIdsAndScanned_thenEqual() {
        List<BigInteger> expectedIds = new ArrayList<>();
        expectedIds.add(new BigInteger("1"));
        expectedIds.add(new BigInteger("2"));
        expectedIds.add(new BigInteger("3"));
        expectedIds.add(new BigInteger("4"));
        expectedIds.add(new BigInteger("5"));
        expectedIds.add(new BigInteger("6"));
        expectedIds.add(new BigInteger("7"));
        expectedIds.add(new BigInteger("8"));

        List<BigInteger> actualIds = ReflectionHelper.getSortedAttributeIds(TestEntity.class);

        Assert.assertEquals(expectedIds, actualIds);
    }

    @ObjectType(ID = "1")
    private class TestEntity extends BaseEntity {
        @ValueField(ID = "1")
        private String name;

        @DateField(ID = "2")
        private LocalDateTime dateTime;

        @ListField(ID = "3")
        private Double aDouble;

        @ReferenceField(ID = "4")
        private BigInteger ref;

        @ValueField(ID = "5")
        private String name2;

        @ValueField(ID = "6")
        private String name3;

        @ValueField(ID = "7")
        private String name4;

        @DateField(ID = "8")
        private String name5;
    }
}
