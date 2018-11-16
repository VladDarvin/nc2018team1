package com.nc.airport.backend.eav.dao.Tests;

import com.nc.airport.backend.eav.mutable.Mutable;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 16.11.2018.
 */
public class MutableFactory {

    /**
     * relevant only for DB with this set-ups:
     *
     *
     INSERT INTO OBJTYPE (OBJECT_TYPE_ID, PARENT_ID, CODE, NAME, DESCRIPTION) VALUES (1, null, 'A', 'AAA', null);
     INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (1, 1, NULL , 'val', 'normal value');
     INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (2, 1, null, 'dt_val', 'date value');
     INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (3, 1, 1, 'lst_val', 'list_value');
     INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (11, 1, null, 'an_val', 'another value');
     INSERT INTO ATTRTYPE (ATTR_ID, OBJECT_TYPE_ID, OBJECT_TYPE_ID_REF, CODE, NAME) VALUES (21, 1, null, 'an_dt_val', 'another date value');
     INSERT INTO LISTS (ATTR_ID, LIST_VALUE_ID, VALUE) VALUES (3, 1, 'haha');
     INSERT INTO LISTS (ATTR_ID, LIST_VALUE_ID, VALUE) VALUES (3, 2, 'AHAH');

     DELETE FROM OBJECTS;
     INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (0, null, 1, 'ZRO', null);
     INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION) VALUES (1, null, 1, 'ONE', null);
     INSERT INTO ATTRIBUTES (ATTR_ID, OBJECT_ID, DATE_VALUE) VALUES (3, 0, sysdate);
     INSERT INTO OBJREFERENCE (ATTR_ID, REFERENCE, OBJECT_ID) VALUES (3, 0, 1);
     COMMIT;
     */
    static long objectId = 1;

    public Mutable createMutable() {
        Mutable mutable = new Mutable();

        mutable.setObjectId(new BigInteger(String.valueOf(++objectId)));
        mutable.setObjectTypeId(new BigInteger("1"));

        mutable.setObjectName("obj"+objectId);

        Map<BigInteger, String> value = new HashMap<>();
        value.put(new BigInteger("1"), "val"+objectId);
        mutable.setValues(value);

        Map<BigInteger, LocalDate> dateValues = new HashMap<>();
        dateValues.put(new BigInteger("2"), LocalDate.now());
        dateValues.put(new BigInteger("21"), LocalDate.ofYearDay(2018, 200));
        mutable.setDateValues(dateValues);

        Map<BigInteger, BigInteger> listValues = new HashMap<>();
        listValues.put(new BigInteger("3"), new BigInteger("1"));
        mutable.setListValues(listValues);

        Map<BigInteger, BigInteger> references = new HashMap<>();
        references.put(new BigInteger("3"), new BigInteger("0"));
        mutable.setReferences(references);

        return mutable;
    }

    public Mutable createMutableForUpdate() {
        Mutable mutable = new Mutable();

        mutable.setObjectId(new BigInteger(String.valueOf(objectId)));
        mutable.setObjectTypeId(new BigInteger("1"));

        mutable.setObjectName("Object #"+objectId);

        Map<BigInteger, String> value = new HashMap<>();
        value.put(new BigInteger("1"), "value of #"+objectId);
        value.put(new BigInteger("11"), "new value of #"+objectId);
        mutable.setValues(value);

        Map<BigInteger, LocalDate> dateValues = new HashMap<>();
        dateValues.put(new BigInteger("2"), LocalDate.of(2018, 11, 15));
        mutable.setDateValues(dateValues);

        Map<BigInteger, BigInteger> listValues = new HashMap<>();
        listValues.put(new BigInteger("3"), new BigInteger("2"));
        mutable.setListValues(listValues);

        Map<BigInteger, BigInteger> references = new HashMap<>();
        references.put(new BigInteger("3"), new BigInteger("0"));
        mutable.setReferences(references);

        return mutable;
    }
}
