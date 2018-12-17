package com.nc.airport.backend.persistence.eav.repository.impl;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.Entity2Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.mutable2query.Mutable2Query;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class DefaultEavCrudRepository<T extends BaseEntity> implements EavCrudRepository<T> {
    private Mutable2Query m2db;
    private Entity2Mutable e2m;

    @Autowired
    public DefaultEavCrudRepository(Mutable2Query m2q, Entity2Mutable e2m) {
        this.e2m = e2m;
        this.m2db = m2q;
    }

    @Override
    public <S extends T> S save(S entity) {
        Mutable mutable = e2m.convertEntityToMutable(entity);
        S updatedEntity;

        Mutable updatedMutable;
        updatedMutable = m2db.sqlUpdate(mutable);
        updatedEntity = (S) e2m.convertMutableToEntity(updatedMutable, entity.getClass());
        log.info("Saved. Got an updated entity back : " + updatedEntity);

        return updatedEntity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
//             TODO MAKE A METHOD THAT SAVES ALL ENTITIES ON DB PART
        List<S> updatedEntities = new ArrayList<>();
        for (S entity : entities) {
            if (entity != null)
                updatedEntities.add(save(entity));
        }

        return updatedEntities;
    }

    @Override
    public Optional<T> findById(BigInteger objectId, Class<T> entityClass) {
        checkNull(objectId);
        checkNull(entityClass);

        Optional<Mutable> optMutable = m2db.getSingleMutable(objectId);
        T searchedEntity = null;
        if (optMutable.isPresent()) {
            searchedEntity = e2m.convertMutableToEntity(optMutable.get(), entityClass);
        }
        return Optional.ofNullable(searchedEntity);
    }

    @Override
    public List<T> findAll(@NotNull Class<T> entityClass) {
        throw new UnsupportedOperationException("List<T> findAll(@NotNull Class<T> entityClass)");
    }

    @Override
//             TODO NAMING, ALSO THINK IF THIS METHOD IS REALLY NEEDED
    public List<T> findAll(Class<T> entityClass, Iterable<BigInteger> objectIds) {
        checkNull(entityClass);

        List<T> entities = new ArrayList<>();
        for (BigInteger objectId : objectIds) {
            Optional<T> result = findById(objectId, entityClass);
            result.ifPresent(entities::add);
        }
        return entities;
    }

    @Override
    public List<T> findSlice(@NotNull Class<T> entityClass, int startRow, int endRow) {
        checkNull(entityClass);

        List<Mutable> mutables;
        mutables = m2db.getMutablesFromDB(ReflectionHelper.getObjTypeId(entityClass),
                ReflectionHelper.getAttributeIds(entityClass),
                startRow,
                endRow);

        List<T> entities = new ArrayList<>();
        for (Mutable mutable : mutables) {
            entities.add(e2m.convertMutableToEntity(mutable, entityClass));
        }
        return entities;
    }

    @Override
    public List<T> findSlice(@NotNull Class<T> entityClass, int startRow, int endRow, List<SortEntity> sortBy, List<FilterEntity> filterBy) {
        return null;
    }


    @Override
    public void delete(T entity) {
        checkNull(entity);

        deleteById(entity.getObjectId());
    }

    @Override
    public void deleteById(BigInteger objectId) {
        checkNull(objectId);

        m2db.sqlDelete(objectId);
        log.info("Deleted object with Object_id " + objectId);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        checkNull(entities);

        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public BigInteger count(Class<T> entityClass) {
        checkNull(entityClass);

        BigInteger objTypeId = ReflectionHelper.getObjTypeId(entityClass);
        return m2db.countById(objTypeId);
    }

    @Override
    public boolean existsById(BigInteger objectId) {
        checkNull(objectId);

        return m2db.existsByObjId(objectId);
    }

    /**
     * Checks if the argument is null. If so, logs and throws an exception.
     *
     * @param o checked argument
     * @throws IllegalArgumentException if argument is null
     */
    private void checkNull(Object o) {
        if (o == null) {
            String message = "Provided object is null";
            RuntimeException exception = new IllegalArgumentException(message);
            log.error(message, exception);
            throw exception;
        }
    }


}
