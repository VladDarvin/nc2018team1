package com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortingDescriptor {
    /**
     * Generate sorting query string from list of sortEntities
     *
     * @param sortEntities list of sortEntities must not be null
     * @return sorting string
     */
    public String doSorting(List<SortEntity> sortEntities) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" ORDER BY ");
        int countOfEntities = sortEntities.size();
        for (SortEntity entity :
                sortEntities) {
            if (entity.getOrder() == null) {
                queryBuilder.append(entity.getType());
            } else if (entity.getOrder()) {
                queryBuilder.append(entity.getType())
                        .append(" ")
                        .append("ASC");
            } else if (!entity.getOrder()) {
                queryBuilder.append(entity.getType())
                        .append(" ")
                        .append("DESC");
            }
            if (countOfEntities != 1) {
                queryBuilder.append(", ");
            }
            countOfEntities--;

        }
        return queryBuilder.toString();
    }

}
