package com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging;

import org.springframework.stereotype.Component;

@Component
public class PagingDescriptor {

    /**
     * Wrap select query into rownum for getting paging
     *
     * @param query select query for wrapping
     * @param from  start position
     * @param to    end position
     * @return query sting wrapped into rownum
     */
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
