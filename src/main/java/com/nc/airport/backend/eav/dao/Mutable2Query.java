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

    public void sqlInsert (Mutable mutable) throws SQLException {
        buildASequence(new InsertSequenceBuilder(connection), mutable);
    }

    public void sqlUpdate(Mutable mutable) throws SQLException {
        buildASequence(new UpdateSequenceBuilder(connection), mutable);
    }

    public void sqlDelete(Mutable mutable) throws SQLException {
        buildASequence(new DeleteSequenceBuilder(connection), mutable);
    }

    private void buildASequence(SequenceBuilder sequenceBuilder, Mutable mutable){
        sequenceBuilder.build(mutable);
    }

    public List<Mutable> sqlInsertMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> failedMutables = new ArrayList<>();
        for (Mutable mutable : mutables)
            try {
                sqlInsert(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to parse mutable "+mutable.getObjectTypeId());
                failedMutables.add(mutable);
            }
        return failedMutables;
    }

    public List<Mutable> sqlUpdateMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> failedMutables = new ArrayList<>();
        for (Mutable mutable : mutables)
            try {
                sqlUpdate(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to parse mutable "+mutable.getObjectTypeId());
                failedMutables.add(mutable);
            }
        return failedMutables;
    }

    public List<Mutable> sqlDeleteMultipleMutables(Collection<Mutable> mutables) {
        List<Mutable> failedMutables = new ArrayList<>();
        for (Mutable mutable : mutables)
            try {
                sqlDelete(mutable);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to parse mutable "+mutable.getObjectTypeId());
                failedMutables.add(mutable);
            }
        return failedMutables;
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
     * @throws SQLException
     */
    public Mutable getSingleMutable(BigInteger objectId, Collection<BigInteger> attributesId) throws SQLException {
        return new LazyDBFetcher(connection)
                .getMutable(objectId, attributesId);
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
     * @param pagingFrom number of the first object included
     * @param pagingTo number of the last object included
     * @return List of Mutable objects with specified attributes (those which was not specified neither will be in
     * the resulting Mutable, nor will be replaced with nulls, they just won`t be there)
     * @throws SQLException #feelsbadman
     */
    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo) throws SQLException {
        return new LazyDBFetcher(connection)
                .getMutables(objType, attributesId, pagingFrom, pagingTo);
    }

    public List<Mutable> getMutablesFromDB(Collection<BigInteger> objectsId,
                                           Collection<BigInteger> attributesId) throws SQLException {
        return null;
    }

    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo, List<SortEntity> sortBy) throws SQLException {
        return null;
    }

    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy, List<FilterEntity> filterBy) throws SQLException {
        return null;
    }
}
