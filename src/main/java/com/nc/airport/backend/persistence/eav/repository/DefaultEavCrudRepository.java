package com.nc.airport.backend.persistence.eav.repository;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.Entity2Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.exception.CrudRepositoryException;
import com.nc.airport.backend.persistence.eav.mutable2query.Mutable2Query;
import com.nc.airport.backend.repository.EavCrudRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.SQLException;
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
        //TODO move the saving logic to m2db
        try {
            if (existsById(mutable.getObjectId())) {
                log.info("Updating mutable : " + mutable);
                updatedMutable = m2db.sqlUpdate(mutable);
            } else {
                log.info("Inserting mutable : " + mutable);
                updatedMutable = m2db.sqlInsert(mutable);
            }
        } catch (SQLException e) {
            String message = "Entity was not saved properly";
            CrudRepositoryException exception = new CrudRepositoryException(message, e, entity);
            log.error("Problems with saving entity", exception);
            throw exception;
        }
        updatedEntity = (S) e2m.convertMutableToEntity(updatedMutable, entity.getClass());
        log.info("Saved. Got an updated entity back : " + updatedEntity);

        return updatedEntity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
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
    public List<T> findAll(Class<T> entityClass) {
        checkNull(entityClass);

        BigInteger objTypeId = ReflectionHelper.getObjTypeId(entityClass);

        List<Mutable> mutables = m2db.getMutablesFromDB(objTypeId);
        List<T> entities = new ArrayList<>();
        for (Mutable mutable : mutables) {
            entities.add(e2m.convertMutableToEntity(mutable, entityClass));
        }
        return entities;
    }

    @Override
    public List<T> findAllById(Iterable<BigInteger> objectIds, Class<T> entityClass) {
        checkNull(entityClass);

        List<T> entities = new ArrayList<>();
        for (BigInteger objectId : objectIds) {
            Optional<T> result = findById(objectId, entityClass);
            result.ifPresent(entities::add);
        }
        return entities;
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
     * Checks if argument is null. If so, logs and throws an exception.
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
