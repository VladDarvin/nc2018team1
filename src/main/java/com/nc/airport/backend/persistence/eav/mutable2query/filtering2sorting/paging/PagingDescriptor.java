package com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging;

import org.springframework.stereotype.Component;

@Component
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
