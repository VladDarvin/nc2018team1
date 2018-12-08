package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.Mutable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.sql.Connection;

/**
 * Created by User on 23.11.2018.
 */
@Deprecated
class MutableFromDBPuller {
    private Logger logger = LogManager.getLogger(MutableFromDBPuller.class.getSimpleName());
    private Connection connection;
    private BigInteger objectId;
    private Mutable mutable;

    public MutableFromDBPuller(Connection connection) {
        this.connection = connection;
    }

//    public List<Mutable> pullMultiple(Collection<BigInteger> objectsId, Collection<BigInteger> attributesId)
//            throws SQLException {
//        List<Mutable> mutables = new ArrayList<>();
//        for (Mutable2Query.QueryingEntity entity : entities) {
//            Mutable mutable = pull(entity.getObjectId(),
//                    entity.getValuesAttrId(),
//                    entity.getDateValuesAttrId(),
//                    entity.getListValuesAttrId(),
//                    entity.getReferencesId());
//            mutables.add(mutable);
//        }
//        return mutables;
//    }
//
//    public Mutable pull(BigInteger objectId,
//                        Collection<BigInteger> valuesAttrId,
//                        Collection<BigInteger> dateValuesAttrId,
//                        Collection<BigInteger> listValuesAttrId,
//                        Collection<BigInteger> referencesId) throws SQLException{
//        this.objectId = objectId;
//        this.mutable = new Mutable();
//        pullGeneralInfo();
//        pullAttributes(valuesAttrId, dateValuesAttrId, listValuesAttrId);
//        pullReferences(referencesId);
//        return mutable;
//    }
//
//    private void pullGeneralInfo() throws SQLException {
//        try (PreparedStatement preparedStatement = connection.prepareStatement(
//                "SELECT PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION FROM OBJECTS WHERE OBJECT_ID = ?"
//        )){
//            preparedStatement.setObject(1, objectId);
//            ResultSet result = preparedStatement.executeQuery();
//            result.next();
//            mutable.setObjectId(objectId);
//            String nextBigInt = result.getString(1);
//            mutable.setParentId(applyBigInt(nextBigInt));
//            nextBigInt = result.getString(2);
//            mutable.setObjectTypeId(applyBigInt(nextBigInt));
//            mutable.setObjectName(result.getString(3));
//            mutable.setObjectDescription(result.getString(4));
//            return;
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to pull general info");
//            throw e;
//        }
//    }
//
//    private boolean noSuchElements(Collection<BigInteger> elements) {
//        return elements == null || elements.size() == 0;
//    }
//
//    private BigInteger applyBigInt(String bigInt) {
//        return bigInt == null ? null : new BigInteger(bigInt);
//    }
//
//    private void pullAttributes(Collection<BigInteger> valuesAttrId,
//                                Collection<BigInteger> dateValuesAttrId,
//                                Collection<BigInteger> listValuesAttrId) throws SQLException {
//        pullValues(valuesAttrId);
//        pullDateValues(dateValuesAttrId);
//        pullListValues(listValuesAttrId);
//    }
//
//    private void pullValues(Collection<BigInteger> valuesAttrId) throws SQLException {
//        if (noSuchElements(valuesAttrId)) return;
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(
//                buildAttributeQuery(valuesAttrId.size(), "VALUE")))
//        {
//            ResultSet result = getValuesToResultSet(valuesAttrId, preparedStatement);
//            Map<BigInteger, String> values = new HashMap<>();
//            while (result.next()) {
//                String value = result.getString(2);
//                if (value == null)
//                    throw new IllegalArgumentException("There is no string value in this attr_id in that object");
//                values.put(new BigInteger(result.getString(1)), value);
//            }
//            mutable.setValues(values);
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to pull values");
//            throw e;
//        }
//    }
//
//    private void pullDateValues(Collection<BigInteger> valuesAttrId) throws SQLException {
//        if (noSuchElements(valuesAttrId)) return;
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(
//                buildAttributeQuery(valuesAttrId.size(), "DATE_VALUE")))
//        {
//            ResultSet result = getValuesToResultSet(valuesAttrId, preparedStatement);
//            Map<BigInteger, LocalDate> values = new HashMap<>();
//            while (result.next()) {
//                LocalDate dateValue = result.getDate(2).toLocalDate();
//                if (dateValue == null)
//                    throw new IllegalArgumentException("There is no date in this attr_id in that object");
//                values.put(new BigInteger(result.getString(1)), dateValue);
//            }
//            mutable.setDateValues(values);
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to pull date values");
//            throw e;
//        }
//    }
//
//    private void pullListValues(Collection<BigInteger> valuesAttrId) throws SQLException {
//        if (noSuchElements(valuesAttrId)) return;
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(
//                buildAttributeQuery(valuesAttrId.size(), "LIST_VALUE_ID")))
//        {
//            ResultSet result = getValuesToResultSet(valuesAttrId, preparedStatement);
//            Map<BigInteger, BigInteger> values = new HashMap<>();
//            while (result.next()) {
//                String listValue = result.getString(2);
//                if (listValue == null)
//                    throw new IllegalArgumentException("There is no list value with such attr_id in this object");
//                values.put(new BigInteger(result.getString(1)), new BigInteger(listValue));
//            }
//            mutable.setListValues(values);
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to pull list values");
//            throw e;
//        }
//    }
//
//    private void pullReferences(Collection<BigInteger> referencesId) throws SQLException{
//        if (noSuchElements(referencesId)) return;
//
//        StringBuilder mainQuery = new StringBuilder("SELECT ATTR_ID, REFERENCE FROM OBJREFERENCE WHERE ATTR_ID = ? AND OBJECT_ID = ").append(objectId);
//        String sql = buildMultiQuery(referencesId.size(), mainQuery);
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
//            ResultSet result = getValuesToResultSet(referencesId, preparedStatement);
//            Map<BigInteger, BigInteger> references = new HashMap<>();
//            while (result.next()) {
//                String refValue = result.getString(2);
//                if (refValue == null)
//                    throw new IllegalArgumentException("There is no reference in such attr_id in this object");
//                references.put(new BigInteger(result.getString(1)), new BigInteger(refValue));
//            }
//            mutable.setReferences(references);
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to pull references");
//            throw e;
//        }
//    }
//
//    private ResultSet getValuesToResultSet(Collection<BigInteger> valuesAttrId,
//                                           PreparedStatement preparedStatement) throws SQLException{
//        Iterator<BigInteger> iterator = valuesAttrId.iterator();
//        BigInteger attrId = null;
//        try {
//            for (int i = 1; i <= valuesAttrId.size(); i++) {
//                attrId = iterator.next();
//                preparedStatement.setObject(i, attrId);
//            }
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Failed to set attribute " + attrId);
//            throw e;
//        }
//        return preparedStatement.executeQuery();
//    }
//
//    private String buildAttributeQuery(int valuesAttrIdSize, String valueType) {
//        StringBuilder mainQuery = new StringBuilder("SELECT ATTR_ID, ").append(valueType)
//                .append(" FROM ATTRIBUTES WHERE ATTR_ID = ? AND OBJECT_ID = ").append(objectId);
//        return buildMultiQuery(valuesAttrIdSize, mainQuery);
//    }
//
//    private String buildMultiQuery(int valuesAttrIdSize, StringBuilder mainQuery) {
//        StringBuilder sql = new StringBuilder(mainQuery);
//        for (int i = 1; i < valuesAttrIdSize - 1; i++)
//            sql.append(" UNION ALL ").append(mainQuery);
//        if (valuesAttrIdSize != 1)
//            sql.append(" UNION ALL ").append(mainQuery);
//        return sql.toString();
//    }
}
