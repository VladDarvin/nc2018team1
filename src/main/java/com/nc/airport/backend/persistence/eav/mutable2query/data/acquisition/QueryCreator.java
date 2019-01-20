package com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.List;

class QueryCreator {


    void logSequence(Logger logger, String fullQuery) {
        logger.log(Level.INFO, "Executing sequence:\n" + fullQuery);
    }


    /*Tall Lazy query*/


    /*
        SELECT * FROM
          (
            SELECT a.*, rownum rnum FROM
            (
              WITH OBJECT_ATTRIBUTES AS
            (
              SELECT ATTR_ID
              FROM ATTRTYPE ATTRT
              JOIN OBJTYPE OBJT
                ON ATTRT.OBJECT_TYPE_ID = OBJT.OBJECT_TYPE_ID
              START WITH OBJT.OBJECT_TYPE_ID = 8
              CONNECT BY OBJT.OBJECT_TYPE_ID = PRIOR PARENT_ID
              GROUP BY ATTR_ID
            )
            SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ATTRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE
              FROM OBJECTS O
            JOIN OBJECT_ATTRIBUTES ATTRT
                ON O.OBJECT_TYPE_ID = 8
            LEFT JOIN ATTRIBUTES A
                ON ATTRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID
            LEFT JOIN OBJREFERENCE R
                ON ATTRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID
            WHERE O.OBJECT_ID = 21
                AND ATTRT.ATTR_ID IN (45, 50, 55, 43, 48)
            ORDER BY O.OBJECT_ID, A.ATTR_ID
            ) a
          WHERE rownum <= 6)
        WHERE rnum >= 1;
     */
    StringBuilder createTallLazyQuery(String objectTypeId, String whereClause) {
        return new StringBuilder
                ("WITH OBJECT_ATTRIBUTES AS ")
                .append("(SELECT ATTR_ID")
                .append(" FROM ATTRTYPE ATTRT")
                .append(" JOIN OBJTYPE OBJT")
                .append("   ON ATTRT.OBJECT_TYPE_ID = OBJT.OBJECT_TYPE_ID")
                .append(" START WITH OBJT.OBJECT_TYPE_ID = ").append(objectTypeId)
                .append(" CONNECT BY OBJT.OBJECT_TYPE_ID = PRIOR PARENT_ID ")
                .append(" GROUP BY ATTR_ID) ")

                .append("SELECT O.OBJECT_ID, PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, DESCRIPTION, ")
                .append("ATTRT.ATTR_ID, VALUE, DATE_VALUE, LIST_VALUE_ID, REFERENCE ")
                .append("FROM OBJECTS O ")
                .append(" JOIN OBJECT_ATTRIBUTES ATTRT ")
                .append("  ON O.OBJECT_TYPE_ID = ").append(objectTypeId)
                .append(" LEFT JOIN ATTRIBUTES A ")
                .append("  ON ATTRT.ATTR_ID = A.ATTR_ID AND O.OBJECT_ID = A.OBJECT_ID ")
                .append(" LEFT JOIN OBJREFERENCE R ")
                .append("  ON ATTRT.ATTR_ID = R.ATTR_ID AND O.OBJECT_ID = R.OBJECT_ID ")
                .append(whereClause)
                .append("ORDER BY O.OBJECT_ID, A.ATTR_ID");
    }




    /* Wide Picky query */


    /*  SELECT * FROM
            ( SELECT a.*, rownum rnum
                FROM
                (SELECT * FROM
                    (
                        SELECT O.OBJECT_ID, O.PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, O.DESCRIPTION,
                               A1.VALUE ATTR51,
                               A2.DATE_VALUE ATTR50,
                               A3.LIST_VALUE_ID ATTR45,
                               A4.REFERENCE ATTR55
                        FROM OBJECTS O
                               LEFT JOIN ATTRIBUTES A1
                                    ON A1.ATTR_ID = 51 AND A1.OBJECT_ID = O.OBJECT_ID
                               LEFT JOIN ATTRIBUTES A2
                                    ON A2.ATTR_ID = 50 AND A2.OBJECT_ID = O.OBJECT_ID
                               LEFT JOIN ATTRIBUTES A3
                                    ON A3.ATTR_ID = 45 AND A3.OBJECT_ID = O.OBJECT_ID
                               LEFT JOIN OBJREFERENCE A4
                                    ON A4.ATTR_ID = 55 AND A4.OBJECT_ID = O.OBJECT_ID
                      )
                     WHERE (ATTR45 = 1 OR ATTR45 = 2) ORDER BY ATTR50 DESC;
                  ) a
                WHERE rownum <= 2)
            WHERE rnum >= 1
    */
    StringBuilder createWidePickyQuery(List<BigInteger> values,
                                         List<BigInteger> dateValues,
                                         List<BigInteger> listValues,
                                         List<BigInteger> references) {
        StringBuilder query = new StringBuilder
                ("  SELECT O.OBJECT_ID, O.PARENT_ID, O.OBJECT_TYPE_ID, O.NAME, O.DESCRIPTION, ");
        transferAttributesSelection(query, values, dateValues, listValues, references);
        query.append(" FROM OBJECTS O ");
        transferAttributesJoin(query, values, dateValues, listValues, references);

        return query;
    }

    private void transferAttributesSelection(StringBuilder transferTo,
                                             List<BigInteger> values,
                                             List<BigInteger> dateValues,
                                             List<BigInteger> listValues,
                                             List<BigInteger> references) {
        int i = 1;
        i = selectEachAttr(values, "VALUE", transferTo, i);
        i = selectEachAttr(dateValues, "DATE_VALUE", transferTo, i);
        i = selectEachAttr(listValues, "LIST_VALUE_ID", transferTo, i);
        selectEachAttr(references, "REFERENCE", transferTo, i);
        transferTo.delete(transferTo.lastIndexOf(","), transferTo.length() - 1);
    }

    private int selectEachAttr(List<BigInteger> attrs,
                               String attrColumnType,
                               StringBuilder transferTo,
                               int i) {

        for (BigInteger attr : attrs) {
            transferTo.append("A").append(i++).append(".").append(attrColumnType)
                    .append(" ATTR").append(attr).append(", ");
        }
        return i;
    }

    private void transferAttributesJoin(StringBuilder transferTo,
                                        List<BigInteger> values,
                                        List<BigInteger> dateValues,
                                        List<BigInteger> listValues,
                                        List<BigInteger> references) {
        int i = 1;
        i = joinEachAttr(values, "ATTRIBUTES", transferTo, i);
        i = joinEachAttr(dateValues, "ATTRIBUTES", transferTo, i);
        i = joinEachAttr(listValues, "ATTRIBUTES", transferTo, i);
        joinEachAttr(references, "OBJREFERENCE", transferTo, i);
    }

    private int joinEachAttr(List<BigInteger> attrs,
                             String attrTableType,
                             StringBuilder transferTo,
                             int i) {

        for (int j = 0; j < attrs.size(); j++) {
            String alias = " A" + (i++);
            transferTo.append(" LEFT JOIN ").append(attrTableType).append(alias)
                    .append(" ON").append(alias).append(".ATTR_ID = ?")
                    .append(" AND").append(alias).append(".OBJECT_ID = O.OBJECT_ID ");
        }
        return i;
    }
}
