package com.nc.airport.backend.eav.dao;

import java.math.BigInteger;
import java.sql.Connection;

import com.nc.airport.backend.eav.filtering.FilterEntity;
import com.nc.airport.backend.eav.filtering.SortEntity;
import com.nc.airport.backend.eav.mutable.Mutable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

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

    public Mutable getSingleMutable(BigInteger objectId, Collection<BigInteger> attributesId) throws SQLException {
        return null;
    }

    public List<Mutable> getMutablesFromDB(Collection<BigInteger> objectsId, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo,
                                           List<SortEntity> sortBy, List<FilterEntity> filterBy) throws SQLException {
        return null;
    }

    public List<Mutable> getMutablesFromDB(BigInteger objType, Collection<BigInteger> attributesId,
                                           int pagingFrom, int pagingTo) throws SQLException {
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
