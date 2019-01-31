package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition.MetaDataDBFetcher;
import com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition.TallLazyDBFetcher;
import com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition.WidePickyDBFetcher;
import com.nc.airport.backend.persistence.eav.mutable2query.data.modification.DeleteSequenceBuilder;
import com.nc.airport.backend.persistence.eav.mutable2query.data.modification.InsertSequenceBuilder;
import com.nc.airport.backend.persistence.eav.mutable2query.data.modification.SequenceBuilder;
import com.nc.airport.backend.persistence.eav.mutable2query.data.modification.UpdateSequenceBuilder;
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
//                          TODO MAKE PAGES BIG INTS
@Component
public class Mutable2Query {
    private final Logger logger = LogManager.getLogger(Mutable2Query.class.getSimpleName());
    private Connection connection;

    @Autowired
    public Mutable2Query(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            String message = "Cannot establish connection";
            logger.error(message, e);
            throw new DatabaseConnectionException("Couldn't get connection from dataSource", e);
        }
    }

    public Mutable2Query(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insert a given mutable object to database
     *
     * @param mutable mutable representation of object to be inserted
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
    public List<Mutable> getMutablesFromDB(List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy) {
        return getMutablesFromDB(values, dateValues, listValues, references, pagingFrom, pagingTo, sortBy, null);
    }

    /**
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
    public List<Mutable> getMutablesFromDB(List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy,
                                           List<FilterEntity> filterBy) {
        return new WidePickyDBFetcher(connection)
                .getMutables(values, dateValues, listValues, references, pagingFrom, pagingTo, sortBy, filterBy);
    }

    /**
     * Tells if object with such object_id exists
     *
     * @param objectId search criteria
     * @return true if exists, false if not. False if objectId is null.
     */
    public boolean existsByObjId(BigInteger objectId) {
        return new MetaDataDBFetcher(connection).existsByObjId(objectId);
    }

    /**
     * Returns number of objects of given type_id
     *
     * @param objTypeId search criteria
     * @return number of objects
     */
    public BigInteger countById(BigInteger objTypeId) {
        return new MetaDataDBFetcher(connection).countById(objTypeId);
    }

    /**
     * Returns number of objects of given type_id
     *
     * @param objTypeId search criteria
     * @param filterBy search criteria
     * @return number of objects
     */
    public BigInteger countById(BigInteger objTypeId, List<FilterEntity> filterBy) {
        throw new UnsupportedOperationException("not supported yet");
    }

    /**
     * Get count of returned items by filtering
     *
     * @param values                values attr_id List
     * @param dateValues            dateValues attr_id List
     * @param listValues            listValues attr_id List
     * @param references            references attr_id List
     * @param filterBy
     * @return Total count of pages
     * @throws BadDBRequestException when arguments are invalid
     * @exception DatabaseConnectionException when there's some problems with database or with it's connection
     * to the server
     */
    public BigInteger countByFilter(List<BigInteger> values,
                                    List<BigInteger> dateValues,
                                    List<BigInteger> listValues,
                                    List<BigInteger> references,
                                    List<FilterEntity> filterBy) {
        int countOfItems = new WidePickyDBFetcher(connection)
                .getCountOfMutables(values, dateValues, listValues, references, filterBy);

        return BigInteger.valueOf(countOfItems == 0 ? 1 : (int)Math.ceil((double)countOfItems / (double)10));
    }


    public List<Mutable> getMutablesFromDBByParentId(List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                         int pagingFrom, int pagingTo, BigInteger parentId, BigInteger objectTypeId) {
        return new WidePickyDBFetcher(connection)
                .getMutablesByParentId(values, dateValues, listValues, references, pagingFrom, pagingTo, parentId, objectTypeId);
    }

    public List<Mutable> getMutablesFromDBByParentId(List<BigInteger> values,
                                                     List<BigInteger> dateValues,
                                                     List<BigInteger> listValues,
                                                     List<BigInteger> references,
                                                     int pagingFrom, int pagingTo, BigInteger parentId, BigInteger objectTypeId,
                                                     List<SortEntity> sortBy,
                                                     List<FilterEntity> filterBy) {
        return new WidePickyDBFetcher(connection)
                .getMutablesByParentId(values, dateValues, listValues, references, pagingFrom, pagingTo, parentId, objectTypeId, sortBy, filterBy);
    }

    public Mutable getSingleMutableByReference(List<BigInteger> values,
                                                         List<BigInteger> dateValues,
                                                         List<BigInteger> listValues,
                                                         List<BigInteger> references,
                                                         BigInteger objectId) {
        return new WidePickyDBFetcher(connection)
                .getSingleMutableByReference(values, dateValues, listValues, references, objectId);
    }

    public List<Mutable> getMutablesByReference(List<BigInteger> values,
                                                      List<BigInteger> dateValues,
                                                      List<BigInteger> listValues,
                                                      List<BigInteger> references,
                                                      BigInteger objectId) {
        return new WidePickyDBFetcher(connection)
                .getMutablesByReference(values, dateValues, listValues, references, objectId);
    }

    public BigInteger getNewObjectId() {
        return new InsertSequenceBuilder(connection).getNewObjectId();
    }
}
