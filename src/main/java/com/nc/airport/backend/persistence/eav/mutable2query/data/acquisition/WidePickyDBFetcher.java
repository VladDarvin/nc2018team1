package com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition;

import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.FilteringToSortingDescriptor;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.paging.PagingDescriptor;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
public class WidePickyDBFetcher {
    private Connection connection;

    public WidePickyDBFetcher(Connection connection) {
        this.connection = connection;
    }

    public List<Mutable> getMutables(List<BigInteger> values,
                                     List<BigInteger> dateValues,
                                     List<BigInteger> listValues,
                                     List<BigInteger> references,
                                     int pagingFrom, int pagingTo,
                                     List<SortEntity> sortBy,
                                     List<FilterEntity> filterBy) {

        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();
        FilteringToSortingDescriptor.DescriptorBuilder descBuilder =
                new FilteringToSortingDescriptor.DescriptorBuilder();

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        if (!Collections.isEmpty(filterBy))
            descBuilder.filter(filterBy);
        if (!Collections.isEmpty(sortBy))
            descBuilder.sort(sortBy);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        StringBuilder filteredSortedQuery = new StringBuilder("SELECT * FROM (").append(basicQuery).append(") ");
        filteredSortedQuery.append(descBuilder.build().getQueryBuilder());
        String fullQuery = paging.getPaging(filteredSortedQuery, pagingFrom, pagingTo);

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    public int getCountOfMutables(List<BigInteger> values,
                                  List<BigInteger> dateValues,
                                  List<BigInteger> listValues,
                                  List<BigInteger> references,
                                  List<FilterEntity> filterBy) {

        QueryCreator queryCreator = new QueryCreator();
//        PreparedStatement statement = null;
//        ResultSet result = null;
        FilteringToSortingDescriptor.DescriptorBuilder descBuilder =
                new FilteringToSortingDescriptor.DescriptorBuilder();
        int countOfItems = 0;

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        if (!Collections.isEmpty(filterBy))
            descBuilder.filter(filterBy);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        StringBuilder countFilteredSortedQuery = new StringBuilder("SELECT COUNT(*) AS total FROM (").append(basicQuery).append(") ");
        countFilteredSortedQuery.append(descBuilder.build().getQueryBuilder());
        String fullQuery = countFilteredSortedQuery.toString();

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy);
            while (result.next()) {
                countOfItems = result.getInt("total");
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return countOfItems;
    }

    public List<Mutable> getMutablesByParentId(List<BigInteger> values,
                                               List<BigInteger> dateValues,
                                               List<BigInteger> listValues,
                                               List<BigInteger> references, int pagingFrom, int pagingTo,
                                               BigInteger parentId, BigInteger objectTypeId) {

        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        basicQuery.append("WHERE O.PARENT_ID = ").append(parentId).append(" AND O.OBJECT_TYPE_ID = ").append(objectTypeId);
        String fullQuery = paging.getPaging(basicQuery, pagingFrom, pagingTo);

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, null)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, null);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    public List<Mutable> getMutablesByParentId(List<BigInteger> values,
                                               List<BigInteger> dateValues,
                                               List<BigInteger> listValues,
                                               List<BigInteger> references, int pagingFrom, int pagingTo,
                                               BigInteger parentId, BigInteger objectTypeId, List<SortEntity> sortBy,
                                               List<FilterEntity> filterBy) {

        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;
        PagingDescriptor paging = new PagingDescriptor();
        FilteringToSortingDescriptor.DescriptorBuilder descBuilder =
                new FilteringToSortingDescriptor.DescriptorBuilder();

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        if (!Collections.isEmpty(filterBy))
            descBuilder.filter(filterBy);
        if (!Collections.isEmpty(sortBy))
            descBuilder.sort(sortBy);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        basicQuery.append("WHERE O.PARENT_ID = ").append(parentId).append(" AND O.OBJECT_TYPE_ID = ").append(objectTypeId);
        StringBuilder filteredSortedQuery = new StringBuilder("SELECT * FROM (").append(basicQuery).append(") ");
        filteredSortedQuery.append(descBuilder.build().getQueryBuilder());
        String fullQuery = paging.getPaging(filteredSortedQuery, pagingFrom, pagingTo);

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, filterBy);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    //TODO refactor
    public List<Mutable> getMutablesByReference(List<BigInteger> values,
                                                List<BigInteger> dateValues,
                                                List<BigInteger> listValues,
                                                List<BigInteger> references,
                                                BigInteger objectId) {

        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        basicQuery.append("WHERE ");

//        we don't need to append 'OR' to the last reference
        int i = 1;
        for (BigInteger reference : references) {
            int indexOfAttrNumber = basicQuery.toString().indexOf(".REFERENCE ATTR" + reference);
            String numberString = reference.toString();
            String attrNumber = basicQuery.substring(indexOfAttrNumber - numberString.length(), indexOfAttrNumber);
            if (attrNumber.contains("A")) {
                attrNumber = attrNumber.substring(1);
            }
            basicQuery.append("A").append(attrNumber).append(".REFERENCE = ").append(objectId);
            if (i < references.size()) {
                basicQuery.append(" OR ");
                i++;
            }
        }
        String fullQuery = basicQuery.toString();

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, null)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, null);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    public List<Mutable> getMutablesBySeveralReferences(List<BigInteger> values,
                                                        List<BigInteger> dateValues,
                                                        List<BigInteger> listValues,
                                                        List<BigInteger> references,
                                                        Map<BigInteger, BigInteger> objectIds) {

        QueryCreator queryCreator = new QueryCreator();
        List<Mutable> mutables = new ArrayList<>();
//        PreparedStatement statement = null;
//        ResultSet result = null;

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        basicQuery.append("WHERE ");

//        we don't need to append 'OR' to the last reference
        int i = 1;
        for (BigInteger reference : references) {
            for (BigInteger attrId : objectIds.keySet()) {
                if (reference.equals(attrId)) {
                    int indexOfAttrNumber = basicQuery.toString().indexOf(".REFERENCE ATTR" + reference);
                    String numberString = reference.toString();
                    String attrNumber = basicQuery.substring(indexOfAttrNumber - numberString.length(), indexOfAttrNumber);
                    if (attrNumber.contains("A")) {
                        attrNumber = attrNumber.substring(1);
                    }

                    basicQuery.append("A").append(attrNumber).append(".REFERENCE = ").append(objectIds.get(attrId));


                    if (i < objectIds.keySet().size()) {
                        basicQuery.append(" AND ");
                        i++;
                    }
                }
            }
        }
        String fullQuery = basicQuery.toString();

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, null)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, null);
            while (result.next()) {
                Mutable mutable = new Mutable();
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
                mutables.add(mutable);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutables;
    }

    private boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public Mutable getSingleMutableByReference(List<BigInteger> values,
                                               List<BigInteger> dateValues,
                                               List<BigInteger> listValues,
                                               List<BigInteger> references,
                                               BigInteger objectId) {

        QueryCreator queryCreator = new QueryCreator();
        Mutable mutable = new Mutable();
//        PreparedStatement statement = null;
//        ResultSet result = null;

        values = ensureNonNullSecurity(values);
        dateValues = ensureNonNullSecurity(dateValues);
        listValues = ensureNonNullSecurity(listValues);
        references = ensureNonNullSecurity(references);

        StringBuilder basicQuery = queryCreator.createWidePickyQuery(values, dateValues, listValues, references);
        basicQuery.append("JOIN objreference oref ON oref.reference = O.OBJECT_ID and oref.object_id =").append(objectId);
        String fullQuery = basicQuery.toString();

        queryCreator.logSequence(log, fullQuery);

        try (PreparedStatement statement = connection.prepareStatement(fullQuery);
             ResultSet result = resultMultipleMutables(statement, values, dateValues, listValues, references, null)) {
//            statement = connection.prepareStatement(fullQuery);
//            result = resultMultipleMutables(statement, values, dateValues, listValues, references, null);
            while (result.next()) {
                pullGeneralInfo(result, mutable);
                pullAttributes(result, mutable, values, dateValues, listValues, references);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Could not open statement", e);
        } finally {
//            closeResultSetAndStatement(result, statement);
        }
        return mutable;
    }

    private void closeResultSetAndStatement(ResultSet result, Statement statement) {
        if (result != null)
            try {
                result.close();
            } catch (SQLException e) {
                log.error(e);
                throw new DatabaseConnectionException("Could not close result set", e);
            }

        if (statement != null)
            try {
                statement.close();
            } catch (SQLException e) {
                log.error(e);
                throw new DatabaseConnectionException("Could not close statement", e);
            }
    }

    private ResultSet resultMultipleMutables(PreparedStatement statement,
                                             List<BigInteger> values,
                                             List<BigInteger> dateValues,
                                             List<BigInteger> listValues,
                                             List<BigInteger> references,
                                             List<FilterEntity> filters) {

        int indexesBefore = 0;
        setAttributes(statement, values, indexesBefore);

        indexesBefore = values.size();
        setAttributes(statement, dateValues, indexesBefore);

        indexesBefore = indexesBefore + dateValues.size();
        setAttributes(statement, listValues, indexesBefore);

        indexesBefore = indexesBefore + listValues.size();
        setAttributes(statement, references, indexesBefore);

        if (filters != null) {
            indexesBefore = indexesBefore + references.size();
            setFilters(statement, filters, indexesBefore);
        }
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Error is found after query execution", e);
        }
    }

    private void setAttributes(PreparedStatement statement,
                               Collection<BigInteger> attributesId,
                               int indexesBefore) {

        try {
            Iterator<BigInteger> attrIterator = attributesId.iterator();
            for (int i = indexesBefore + 1; i <= attributesId.size() + indexesBefore; i++) {
                BigInteger attrId = attrIterator.next();
                statement.setObject(i, attrId);
            }
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Could not set given attributes", e);
        }
    }

    private void setFilters(PreparedStatement statement,
                            List<FilterEntity> filters,
                            int indexesBefore) {

        try {
            Iterator<FilterEntity> attrIterator = filters.iterator();
            for (int i = indexesBefore + 1; i <= getFiltersGlobalSize(filters) + indexesBefore; i++) {
                Set<Object> filterValues = attrIterator.next().getValues();

                Iterator<Object> filterValuesIterator = filterValues.iterator();
                int j;
                for (j = i; j < filterValues.size() + i; j++) {
                    Object filterVal = filterValuesIterator.next();
                    statement.setObject(j, filterVal);
                }
                i = j - 1;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Could not set given filters", e);
        }
    }

    private int getFiltersGlobalSize(List<FilterEntity> filters) {
        int globalSize = 0;
        for (FilterEntity entity : filters) {
            globalSize += entity.getValues().size();
        }
        return globalSize;
    }

    private void pullGeneralInfo(ResultSet result, Mutable mutable) {
        try {
            mutable.setObjectId(applyBigInt(1, result));
            mutable.setParentId(applyBigInt(2, result));
            mutable.setObjectTypeId(applyBigInt(3, result));
            mutable.setObjectName(result.getString(4));
            mutable.setObjectDescription(result.getString(5));
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Database doesn't contain requested object", e);
        }
    }

    private void pullAttributes(ResultSet result,
                                Mutable mutable,
                                List<BigInteger> values,
                                List<BigInteger> dateValues,
                                List<BigInteger> listValues,
                                List<BigInteger> references) {
        try {
            int indexesBefore = 5; //general object information
            pullValues(result, mutable, values, indexesBefore);

            indexesBefore = indexesBefore + values.size();
            pullDateValues(result, mutable, dateValues, indexesBefore);

            indexesBefore = indexesBefore + dateValues.size();
            pullListValues(result, mutable, listValues, indexesBefore);

            indexesBefore = indexesBefore + listValues.size();
            pullReferences(result, mutable, references, indexesBefore);
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Couldn`t pull given attributes", e);
        }
    }

    //fixme try to make next 4 methods less hardcoded
    private void pullValues(ResultSet result,
                            Mutable mutable,
                            List<BigInteger> valuesIn,
                            int indexesBefore) throws SQLException {

        Map<BigInteger, String> valuesOut = new HashMap<>();
        Iterator<BigInteger> attrIterator = valuesIn.iterator();
        for (int i = indexesBefore + 1; i <= valuesIn.size() + indexesBefore; i++) {
            BigInteger attrId = attrIterator.next();
            valuesOut.put(attrId, result.getString(i));
        }
        mutable.setValues(valuesOut);
    }

    private void pullDateValues(ResultSet result,
                                Mutable mutable,
                                List<BigInteger> dateValuesIn,
                                int indexesBefore) throws SQLException {

        Map<BigInteger, LocalDateTime> dateValuesOut = new HashMap<>();
        Iterator<BigInteger> attrIterator = dateValuesIn.iterator();
        for (int i = indexesBefore + 1; i <= dateValuesIn.size() + indexesBefore; i++) {
            BigInteger attrId = attrIterator.next();
            Timestamp stamp = result.getTimestamp(i);
            dateValuesOut.put(attrId, stamp != null ? stamp.toLocalDateTime() : null);
        }
        mutable.setDateValues(dateValuesOut);
    }

    private void pullListValues(ResultSet result,
                                Mutable mutable,
                                List<BigInteger> listValuesIn,
                                int indexesBefore) throws SQLException {

        Map<BigInteger, BigInteger> listValuesOut = new HashMap<>();
        Iterator<BigInteger> attrIterator = listValuesIn.iterator();
        for (int i = indexesBefore + 1; i <= listValuesIn.size() + indexesBefore; i++) {
            BigInteger attrId = attrIterator.next();
            listValuesOut.put(attrId, applyBigInt(i, result));
        }
        mutable.setListValues(listValuesOut);
    }

    private void pullReferences(ResultSet result,
                                Mutable mutable,
                                List<BigInteger> referencesIn,
                                int indexesBefore) throws SQLException {

        Map<BigInteger, BigInteger> referencesOut = new HashMap<>();
        Iterator<BigInteger> attrIterator = referencesIn.iterator();
        for (int i = indexesBefore + 1; i <= referencesIn.size() + indexesBefore; i++) {
            BigInteger attrId = attrIterator.next();
            referencesOut.put(attrId, applyBigInt(i, result));
        }
        mutable.setReferences(referencesOut);
    }

    private BigInteger applyBigInt(String bigInt) {
        return bigInt == null ? null : new BigInteger(bigInt);
    }

    private BigInteger applyBigInt(int columnNumber, ResultSet result) throws SQLException {
        return applyBigInt(result.getString(columnNumber));
    }

    private List<BigInteger> ensureNonNullSecurity(List<BigInteger> list) {
        if (list == null) {
            return new ArrayList<>();
        } else return list;
    }
}
