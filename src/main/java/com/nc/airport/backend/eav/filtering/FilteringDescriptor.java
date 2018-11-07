package com.nc.airport.backend.eav.filtering;

import java.util.Queue;

class FilteringDescriptor {

    private StringBuilder queryBuilder = new StringBuilder();

    String doFiltering(Queue<FilterEntity> filterEntities) {
        queryBuilder.append("WHERE (");
        int countOfEntities = filterEntities.size();
        for (FilterEntity entity:
                filterEntities) {
            int countOfValues = entity.getValues().size();
            for (Object value:
                 entity.getValues()) {
                if (value instanceof String) {
                    queryBuilder.append("LOWER(")
                            .append(entity.getType())
                            .append(") LIKE LOWER('%")
                            .append(value)
                            .append("%')");
                } else if (value instanceof Integer) {
                    queryBuilder.append(entity.getType())
                            .append(" = ")
                            .append(value);
                }
                if (countOfValues != 1) {
                    queryBuilder.append(" OR ");
                } else {
                    queryBuilder.append(")");
                }
                countOfValues--;
            }
            if (countOfEntities != 1) {
                queryBuilder.append(" AND (");
            }
            countOfEntities--;
        }
        return queryBuilder.toString();
    }

}
