package com.nc.airport.backend.eav.dao;

import java.math.BigInteger;
import java.sql.Connection;

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

    /**
     * If there is no values in column that you want to sort by, just leave the corresponding collection null
     * @param valuesAttrId      }
     * @param dateValuesAttrId  } values by which sorting is performed
     * @param listValuesAttrId  }
     * @param order determines the order in which values go after ORDER BY clause. Can be in range 1-3
     * @param ascending determines if the order ascending (true) or descending (false)
     */
    public Queue<BigInteger> getObjectsIdSortedBy(Collection<BigInteger> valuesAttrId,
                                                  Collection<BigInteger> dateValuesAttrId,
                                                  Collection<BigInteger> listValuesAttrId,
                                                  Queue<Integer> order,
                                                  boolean ascending) {
        return null;        //TODO
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

    /** If there is no values in column that you want to sort by,
     *  just leave the corresponding collection null or as empty collections
     *  @param referencesId BigInteger - attr_id
     */
    public Mutable getMutableFromDB(BigInteger objectId,
                                    Collection<BigInteger> valuesAttrId,
                                    Collection<BigInteger> dateValuesAttrId,
                                    Collection<BigInteger> listValuesAttrId,
                                    Collection<BigInteger> referencesId) throws SQLException {
        return new MutableFromDBPuller(connection)
                .pull(objectId, valuesAttrId, dateValuesAttrId, listValuesAttrId, referencesId);
    }

    public List<Mutable> getMultipleMutablesFromDB(Collection<QueryingEntity> entities) throws SQLException{
        return new MutableFromDBPuller(connection).pullMultiple(entities);
    }

    //If there is no values in column that you want to sort by, just leave the corresponding collection null
    //@param referencesId BigInteger - attr_id
    public class QueryingEntity {
        private BigInteger objectId;
        private Collection<BigInteger> valuesAttrId;
        private Collection<BigInteger> dateValuesAttrId;
        private Collection<BigInteger> listValuesAttrId;
        private Collection<BigInteger> referencesId;

        public QueryingEntity() {}

        public QueryingEntity(BigInteger objectId, Collection<BigInteger> valuesAttrId,
                              Collection<BigInteger> dateValuesAttrId,
                              Collection<BigInteger> listValuesAttrId,
                              Collection<BigInteger> referencesId) {
            this.objectId = objectId;
            this.valuesAttrId = valuesAttrId;
            this.dateValuesAttrId = dateValuesAttrId;
            this.listValuesAttrId = listValuesAttrId;
            this.referencesId = referencesId;
        }

        public BigInteger getObjectId() {
            return objectId;
        }

        public void setObjectId(BigInteger objectId) {
            this.objectId = objectId;
        }

        public Collection<BigInteger> getValuesAttrId() {
            return valuesAttrId;
        }

        public void setValuesAttrId(Collection<BigInteger> valuesAttrId) {
            this.valuesAttrId = valuesAttrId;
        }

        public Collection<BigInteger> getDateValuesAttrId() {
            return dateValuesAttrId;
        }

        public void setDateValuesAttrId(Collection<BigInteger> dateValuesAttrId) {
            this.dateValuesAttrId = dateValuesAttrId;
        }

        public Collection<BigInteger> getListValuesAttrId() {
            return listValuesAttrId;
        }

        public void setListValuesAttrId(Collection<BigInteger> listValuesAttrId) {
            this.listValuesAttrId = listValuesAttrId;
        }

        public Collection<BigInteger> getReferencesId() {
            return referencesId;
        }

        public void setReferencesId(Collection<BigInteger> referencesId) {
            this.referencesId = referencesId;
        }
    }
}
