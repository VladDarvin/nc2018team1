package com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Log4j2
@Component
public class FilteringDescriptor {

    /**
     * Generate filtering query string from list of FilterEntities
     *
     * @param filterEntities list of FilterEntities must not be null
     * @return filtering string
     */
    public String doFiltering(List<FilterEntity> filterEntities) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" WHERE (");
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
                } else if (value instanceof BigInteger) {
                    queryBuilder.append(entity.getType())
                            .append(" = ?");
                } else {
                    // TODO: 18.12.2018 either throw an exception or log and continue work
                }
                    //FIXME if instanceof LocalDateTime ?
                else {
                    log.warn("Filter entities can only have value of String, BigInteger or LocalDateTime/n" +
                            "Don't use other types");
                    throw new BadDBRequestException("Illegal filter entity value", null);
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
