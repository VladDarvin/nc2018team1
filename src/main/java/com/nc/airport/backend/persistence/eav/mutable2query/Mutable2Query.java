package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class Mutable2Query {
    private final Logger logger = LogManager.getLogger(Mutable2Query.class.getSimpleName());
    private Connection connection;

    public Mutable2Query(@Autowired DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            String message = "Cannot establish connection";
            logger.error(message, e);
            throw new DatabaseConnectionException("Couldn't get connection from dataSource", e);
        }
    }

    /**
     * Insert a given mutable object to database
     *
     * @param mutable mutable representation of object to be inserted
     * @return
     */
    public Mutable sqlInsert(Mutable mutable) {
        return buildASequence(new InsertSequenceBuilder(connection), mutable);
    }


    /**
     * Update or insert a database object represented by given mutable.
     * Updates if mutable has a preset objectId.
     * Inserts if mutable has a null value of objectId.
     *
     * @param mutable mutable representation of object to be updated
     */
    public Mutable sqlUpdate(Mutable mutable) {
        return buildASequence(new UpdateSequenceBuilder(connection), mutable);
    }

    /**
     * Deletes an object of given object_id
     *
     * @param objectId id of object to be deleted
     */
    public Mutable sqlDelete(BigInteger objectId) {
        return new DeleteSequenceBuilder(connection).build(objectId);
    }

    /**
     * Deletes a database object represented by given mutable
     *
     * @param mutable mutable representation of object to be deleted
     */
    public Mutable sqlDelete(Mutable mutable) {
        return buildASequence(new DeleteSequenceBuilder(connection), mutable);
    }

    private Mutable buildASequence(SequenceBuilder sequenceBuilder, Mutable mutable) {
        return sequenceBuilder.build(mutable);
    }

    public List<Mutable> sqlInsertMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> insertedMutables = new ArrayList<>();

        for (Mutable mutable : mutables) {
            sqlInsert(mutable);
            insertedMutables.add(mutable);
        }

        return insertedMutables;
    }

    public List<Mutable> sqlUpdateMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> updatedMutables = new ArrayList<>();
        for (Mutable mutable : mutables) {
            sqlUpdate(mutable);
            updatedMutables.add(mutable);
        }

        return updatedMutables;
    }

    public List<Mutable> sqlDeleteMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> deletedMutables = new ArrayList<>();
        for (Mutable mutable : mutables) {
            sqlDelete(mutable);
            deletedMutables.add(mutable);
        }
        return deletedMutables;
    }

    /**
     * Fetching one Mutable object from database
     * with specified attributes of object or of any of object`s parents
     *
     * @param objectId     ID of the object in OBJECTS table which is also stored or will be included
     *                     in objectId field of specified Mutable object
     * @param attributesId Collection of IDs of all attributes (values, date_values, list_value_ids and references)
     *                     included in each Mutable object
     * @return Mutable object with specified attributes (those which was not specified neither will be in
     * the resulting Mutable, nor will be replaced with nulls, they just won`t be there)
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     */
    public Mutable getSingleMutable(BigInteger objectId, Collection<BigInteger> attributesId) {
        return new TallLazyDBFetcher(connection)
                .getMutable(objectId, attributesId);
    }

    /**
     * Fetching multiple Mutable objects from database
     * with specified attributes of object or of any of object`s parents
     *
     * @param objType      ID of the object type in OBJTYPE table which is also stored or will be included
     *                     in objectTypeId field of specified Mutable object
     * @param attributesId Collection of IDs of all attributes (values, date_values, list_value_ids and references)
     *                     included in each Mutable object
     * @param pagingFrom   number of the first object included. must be >= 1
     * @param pagingTo     number of the last object included. must be >= 1
     * @return List of Mutable objects with specified attributes
     * (those which was not specified neither will be in the resulting Mutable,
     * nor will be replaced with nulls, they just won`t be there)
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     * @see if your objectsId is of objects, that can contain your attributesId
     * this means that attributesId must be inherited or declared in objects' class
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo) {
        return new TallLazyDBFetcher(connection)
                .getMutables(objType, attributesId, pagingFrom, pagingTo);
    }

    /**
     * Fetching multiple Mutable objects from database
     * with specified attributes of object or of any of object`s parents
     *
     * @param objectsId    Collection of objects Id
     * @param attributesId Collection of all attributes (values, date_values, list_value_ids and references)
     * @return List of Mutable objects with specified attributes
     * (those which was not specified neither will be in the resulting Mutable,
     * nor will be replaced with nulls, they just won`t be there)
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     * @see if your objectsId is of objects, that can contain your attributesId
     * this means that attributesId must be inherited or declared in objects' class
     */
    public List<Mutable> getMutablesFromDB(List<BigInteger> objectsId,
                                           Collection<BigInteger> attributesId) {
        return new TallLazyDBFetcher(connection)
                .getMutables(objectsId, attributesId);
    }

    /**
     * @param objType               type of objects
     * @param values                values attr_id List
     * @param dateValues            dateValues attr_id List
     * @param listValues            listValues attr_id List
     * @param references            references attr_id List
     * @param pagingFrom
     * @param pagingTo
     * @param sortBy
     * @return List of Mutable objects with specified attributes
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType,
                                           List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy) {
        return null;
    }

    /**
     * @param objType               type of objects
     * @param values                values attr_id List
     * @param dateValues            dateValues attr_id List
     * @param listValues            listValues attr_id List
     * @param references            references attr_id List
     * @param pagingFrom
     * @param pagingTo
     * @param sortBy
     * @param filterBy
     * @return List of Mutable objects with specified attributes
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType,
                                           List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy,
                                           List<FilterEntity> filterBy) {
        return null;
    }

    /**
     * Tells if object with such object_id exists
     *
     * @param objectId search criteria
     * @return true if exists, false if not. False if objectId is null.
     */
    public boolean existsByObjId(BigInteger objectId) {
        if (objectId == null)
            return false;

        //TODO implement
        throw new UnsupportedOperationException("boolean existsByObjId(BigInteger objectId) not supported yet");
    }

    /**
     * Returns a single mutable with specified object id and all possible attributes
     *
     * @param objectId search criteria
     * @return the mutable with given objectId or {@literal Optional#empty()} if none found
     */
    public Optional<Mutable> getSingleMutable(BigInteger objectId) {
        //TODO implement
        throw new UnsupportedOperationException("Mutable getSingleMutable(BigInteger objectId) is not supported yet");
    }

    /**
     * Returns number of objects of given type_id
     *
     * @param objTypeId search criteria
     * @return number of objects
     */
    public BigInteger countById(BigInteger objTypeId) {
        //TODO implement
        throw new UnsupportedOperationException("BigInteger countById(BigInteger objTypeId)");
    }
}
