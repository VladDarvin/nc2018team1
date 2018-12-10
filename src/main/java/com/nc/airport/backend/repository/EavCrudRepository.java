package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigInteger;
import java.util.List;
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
    <S extends T> List<S> saveAll(Iterable<S> entities);

    /**
     * Retrieves an entity by its object_id.
     *
     * @param objectId must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    Optional<T> findById(BigInteger objectId);

    /**
     * Returns all objects of the class that is supplied.
     *
     * @return all entities
     */
    List<T> findAll(Class<T> entityClass);

    /**
     * Returns all instances with the given object_ids.
     */
    List<T> findAllById(Iterable<BigInteger> objectIds);

    /**
     * Deletes a given entity.
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the entity with the given object_id.
     *
     * @param objectId must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void deleteById(BigInteger objectId);

    /**
     * Deletes the given entities.
     *
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    BigInteger count(Class<T> entityClass);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param objectId must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    boolean existsById(BigInteger objectId);
}
