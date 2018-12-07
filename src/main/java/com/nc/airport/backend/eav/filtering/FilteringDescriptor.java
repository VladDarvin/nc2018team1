package com.nc.airport.backend.eav.filtering;

import java.util.List;

class FilteringDescriptor {

    String doFiltering(List<FilterEntity> filterEntities) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("WHERE (");
        int countOfEntities = filterEntities.size();
        for (FilterEntity entity :
                filterEntities) {
            int countOfValues = entity.getValues().size();
            for (Object value :
                    entity.getValues()) {
                if (value instanceof String) {
                    queryBuilder.append("LOWER(")
                            .append(entity.getType())
                            .append(") LIKE LOWER(?)");
                } else if (value instanceof Integer) {
                    queryBuilder.append(entity.getType())
                            .append(" = ?");
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
