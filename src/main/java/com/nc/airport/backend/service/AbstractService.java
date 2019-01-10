package com.nc.airport.backend.service;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;

import java.math.BigInteger;
import java.util.*;

public abstract class AbstractService<T extends BaseEntity> {
    protected EavCrudRepository<T> repository;
    private Class<T> domainClass;

    public AbstractService(Class<T> domainClass, EavCrudRepository<T> repository) {
        this.domainClass = domainClass;
        this.repository = repository;
    }

    List<FilterEntity> makeFilterList(String search, Class<T> entityClass) {
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

    public ResponseFilteringWrapper<T> filterAndSortEntities(int page, String searchRequest, List<SortEntity> sortEntities) {
        List<FilterEntity> filterEntities = makeFilterList(searchRequest, domainClass);
        List<T> foundEntities = repository.findSlice(domainClass, new Page(page - 1), sortEntities, filterEntities);
        BigInteger countOfPages = repository.count(domainClass, filterEntities);
        return new ResponseFilteringWrapper<>(foundEntities, countOfPages);
    }

    public List<T> getTenEntities(int page) {
        return repository.findSlice(domainClass, new Page(page - 1));
    }

    public T saveEntity(T entity) {
        return repository.save(entity);
    }

    public void deleteEntity(BigInteger id) {
        repository.deleteById(id);
    }

    public BigInteger getEntitiesAmount() {
        return repository.count(domainClass);
    }

    public BigInteger getAmountOfFilteredEntities(String searchString) {
        List<FilterEntity> filterBy = makeFilterList(searchString, domainClass);
        return repository.count(domainClass, filterBy);
    }
}
