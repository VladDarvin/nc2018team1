package com.nc.airport.backend.eav.filtering;

import java.util.List;

class SortingDescriptor {

    String doSorting(List<SortEntity> sortEntities) {
        StringBuilder queryBuilder = new StringBuilder();
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
