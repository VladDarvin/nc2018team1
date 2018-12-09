package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 * The type should implement BaseEntity.
 */
@NoRepositoryBean
public interface EavCrudRepository<T extends BaseEntity> {

    /**
     * Saves a given entity.
     * Use the returned instance for further operations as the save operation might
     * have changed the entity instance completely.
     *
     * @param entity - must not be null
     * @return the saved entity will never be null.
     */
    <S extends T> S save(S entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be null.
     * @return the saved entities will never be null.
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<T> findById(BigInteger id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     */
    Iterable<T> findAllById(Iterable<T> ids);

    /**
     * Deletes a given entity.
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void deleteById(BigInteger id);

    /**
     * Deletes the given entities.
     *
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void deleteAll(Iterable<? extends T> entities);
}
