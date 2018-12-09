package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.BaseEntity;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class DefaultEavCrudRepository<T extends BaseEntity> implements EavCrudRepository<T> {
    @Override
    public <S extends T> S save(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> findById(BigInteger id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<T> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<T> findAllById(Iterable<T> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(BigInteger id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        throw new UnsupportedOperationException();
    }
}
