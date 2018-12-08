package com.nc.airport.backend.persistence.eav.filtering;

public class PagingDescriptor {

    public String getPaging(StringBuilder query, int from, int to) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ( SELECT a.*, rownum rnum FROM (")
                .append(query)
                .append(") a WHERE rownum <= ")
                .append(to)
                .append(") WHERE rnum >= ")
                .append(from);

        return queryBuilder.toString();
    }

}
