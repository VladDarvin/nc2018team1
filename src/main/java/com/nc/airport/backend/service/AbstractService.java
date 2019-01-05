package com.nc.airport.backend.service;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;

import java.math.BigInteger;
import java.util.*;

public abstract class AbstractService<T extends BaseEntity> {
    protected List<FilterEntity> makeFilterList(String search, Class<T> entityClass) {
        String searchString = "%" + search + "%";
        List<BigInteger> attributeIds = ReflectionHelper.getAttributeIds(entityClass);
        Map<BigInteger, Set<Object>> filtering = new HashMap<>();
        for (BigInteger id :
                attributeIds) {
            filtering.put(id, new HashSet<>(Arrays.asList(searchString)));
        }

        List<FilterEntity> filterEntities = new ArrayList<>();
        for (BigInteger key :
                filtering.keySet()) {
            filterEntities.add(new FilterEntity(key, filtering.get(key)));
        }
        return filterEntities;
    }
}
