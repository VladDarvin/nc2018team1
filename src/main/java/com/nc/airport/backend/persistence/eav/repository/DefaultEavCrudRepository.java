package com.nc.airport.backend.persistence.eav.repository;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.Entity2Mutable;
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
    private Mutable2Query m2q;
    private Entity2Mutable e2m;

    @Autowired
    public DefaultEavCrudRepository(Mutable2Query m2q, Entity2Mutable e2m) {
        this.e2m = e2m;
        this.m2q = m2q;
    }

    @Override
    public <S extends T> S save(S entity) { //TODO first check if it exists in DB, then create or update
        Mutable mutable = e2m.convertEntityToMutable(entity);
        S updatedEntity;
        try {
            mutable = m2q.sqlInsert(mutable);
            updatedEntity = (S) e2m.convertMutableToEntity(mutable, entity.getClass());
        } catch (SQLException e) {
            String message = "Entity was not saved properly";
            CrudRepositoryException exception = new CrudRepositoryException(message, e, entity);
            log.error("Problems with saving entity", exception);
            throw exception;
        }
        return updatedEntity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> updatedEntities = new ArrayList<>();
        for (S entity : entities) {
            updatedEntities.add(save(entity));
        }

        return updatedEntities;
    }

    @Override
    public Optional<T> findById(BigInteger objectId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAll(Class<T> entityClass) {
        List list = new ArrayList();
        Airline airline = new Airline();
        airline.setName("name1");
        list.add(airline);
        return list;
    }

    @Override
    public List<T> findAllById(Iterable<BigInteger> objectIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(BigInteger objectId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigInteger count(Class<T> entityClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(BigInteger objectId) {
        throw new UnsupportedOperationException();
    }
}
