package com.nc.airport.backend.eav.filtering;

public class PagingDescriptor {

    public String getPaging(String query, int from, int to) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ( SELECT rownum rnum, a.* FROM (")
                .append(query)
                .append(") a WHERE rownum <= ")
                .append(from)
                .append(") WHERE rnum >= ")
                .append(to)
                .append(";");

        return queryBuilder.toString();
    }

}
