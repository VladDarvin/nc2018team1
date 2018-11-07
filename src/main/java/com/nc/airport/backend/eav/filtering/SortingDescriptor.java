package com.nc.airport.backend.eav.filtering;

import java.util.Queue;

class SortingDescriptor {

    private StringBuilder queryBuilder = new StringBuilder();

    String doSorting(Queue<SortEntity> sortEntities) {
        queryBuilder.append(" ORDER BY ");
        int countOfEntities = sortEntities.size();
        for (SortEntity entity:
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
