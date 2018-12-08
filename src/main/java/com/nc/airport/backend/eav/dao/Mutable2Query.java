package com.nc.airport.backend.eav.dao;

import com.nc.airport.backend.eav.filtering.FilterEntity;
import com.nc.airport.backend.eav.filtering.SortEntity;
import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Mutable2Query {
    private Connection connection;
    private final Logger logger = LogManager.getLogger(Mutable2Query.class.getSimpleName());

    public Mutable2Query(Connection connection) throws SQLException{
        this.connection = connection;
    }

    public Mutable sqlInsert(Mutable mutable) throws SQLException {
        return buildASequence(new InsertSequenceBuilder(connection), mutable);
    }

    public Mutable sqlUpdate(Mutable mutable) throws SQLException {
        return buildASequence(new UpdateSequenceBuilder(connection), mutable);
    }

    public Mutable sqlDelete(Mutable mutable) throws SQLException {
        return buildASequence(new DeleteSequenceBuilder(connection), mutable);
    }

    private Mutable buildASequence(SequenceBuilder sequenceBuilder, Mutable mutable) throws SQLException {
        return sequenceBuilder.build(mutable);
    }

    public List<Mutable> sqlInsertMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> insertedMutables = new ArrayList<>();

        for (Mutable mutable : mutables)
            try {
                sqlInsert(mutable);
                insertedMutables.add(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to insert mutable named "+mutable.getObjectName());
            }
        return insertedMutables;
    }

    public List<Mutable> sqlUpdateMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> updatedMutables = new ArrayList<>();
        for (Mutable mutable : mutables)
            try {
                sqlUpdate(mutable);
                updatedMutables.add(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to update mutable named "+mutable.getObjectName());
            }
        return updatedMutables;
    }

    public List<Mutable> sqlDeleteMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> deletedMutables = new ArrayList<>();
        for (Mutable mutable : mutables)
            try {
                sqlDelete(mutable);
                deletedMutables.add(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to delete mutable named "+mutable.getObjectName());
            }
        return deletedMutables;
    }

    /**
     * Fetching one Mutable object from database
     * with specified attributes of object or of any of object`s parents
     *
     * Each Map is filled in ascending order of attr_id, including attributes that was inherited from parents
     *
     * @param objectId ID of the object in OBJECTS table which is also stored or will be included
     *                in objectId field of specified Mutable object
     * @param attributesId Collection of IDs of all attributes (values, date_values, list_value_ids and references)
     *                    included in each Mutable object
     * @return Mutable object with specified attributes (those which was not specified neither will be in
     * the resulting Mutable, nor will be replaced with nulls, they just won`t be there)
     * @throws SQLException #feelsbadman
     */
    public Mutable getSingleMutable(BigInteger objectId, Collection<BigInteger> attributesId) throws SQLException {
        return new TallLazyDBFetcher(connection)
                .getMutable(objectId, attributesId);
    }

    public List<Mutable> getFullMutables(BigInteger objType,
                                         int pagingFrom, int pagingTo) throws SQLException {


        return new TallLazyDBFetcher(connection).new FullObjectsFetcher().getMutables(objType, pagingFrom, pagingTo);
    }

    /**
     * Fetching multiple Mutable objects from database
     * with specified attributes of object or of any of object`s parents
     *
     * Each Map is filled in ascending order of attr_id, including attributes that was inherited from parents
     *
     * @param objType ID of the object type in OBJTYPE table which is also stored or will be included
     *                in objectTypeId field of specified Mutable object
     * @param attributesId Collection of IDs of all attributes (values, date_values, list_value_ids and references)
     *                    included in each Mutable object
     * @param pagingFrom number of the first object included. must be >= 1
     * @param pagingTo number of the last object included. must be >= 1
     * @return List of Mutable objects with specified attributes
     * (those which was not specified neither will be in the resulting Mutable,
     *  nor will be replaced with nulls, they just won`t be there)
     * @throws SQLException might have multiple meanings
     *          case = Exhausted Resultset - means you tried to put attributes to object which originally
     *          belongs to a child of the object or doesn't belong to object's hierarchical branch at all
     * @see if your objectsId is of objects, that can contain your attributesId
     * this means that attributesId must be inherited or declared in objects' class
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo) throws SQLException {
        return new TallLazyDBFetcher(connection)
                .getMutables(objType, attributesId, pagingFrom, pagingTo);
    }

    /**
     * Fetching multiple Mutable objects from database
     * with specified attributes of object or of any of object`s parents
     *
     * Each Map is filled in ascending order of attr_id, including attributes that was inherited from parents
     *
     * @param objectsId Collection of objects Id
     * @param attributesId Collection of all attributes (values, date_values, list_value_ids and references)
     * @return List of Mutable objects with specified attributes
     *          (those which was not specified neither will be in the resulting Mutable,
     *           nor will be replaced with nulls, they just won`t be there)
     * @throws SQLException might have multiple meanings
     *          case = Exhausted Resultset - means you tried to put attributes to object which originally
     *          belongs to a child of the object or doesn't belong to object's hierarchical branch at all
     * @see if your objectsId is of objects, that can contain your attributesId
     * this means that attributesId must be inherited or declared in objects' class
     */
    public List<Mutable> getMutablesFromDB(Collection<BigInteger> objectsId,
                                           Collection<BigInteger> attributesId) throws SQLException {
        return new TallLazyDBFetcher(connection)
                .getMutables(objectsId, attributesId);
    }

    /**
     *
     * @param objType type of objects
     * @param orderValuesInsideMaps true - values in each Map is in ascending order
     *                              false - values in each Map is in order of passed List
     * @param values values attr_id List
     * @param dateValues dateValues attr_id List
     * @param listValues listValues attr_id List
     * @param references references attr_id List
     * @param pagingFrom
     * @param pagingTo
     * @param sortBy
     * @return List of Mutable objects with specified attributes
     * @throws SQLException
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType,
                                           boolean orderValuesInsideMaps,
                                           List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy) throws SQLException {
            return null;
    }

    /**
     *
     * @param objType type of objects
     * @param orderValuesInsideMaps true - values in each Map is in ascending order
     *                              false - values in each Map is in order of passed List
     * @param values values attr_id List
     * @param dateValues dateValues attr_id List
     * @param listValues listValues attr_id List
     * @param references references attr_id List
     * @param pagingFrom
     * @param pagingTo
     * @param sortBy
     * @param filterBy
     * @return List of Mutable objects with specified attributes
     * @throws SQLException
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType,
                                           boolean orderValuesInsideMaps,
                                           List<BigInteger> values,
                                           List<BigInteger> dateValues,
                                           List<BigInteger> listValues,
                                           List<BigInteger> references,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy,
                                           List<FilterEntity> filterBy) throws SQLException {
        return null;
    }
}
