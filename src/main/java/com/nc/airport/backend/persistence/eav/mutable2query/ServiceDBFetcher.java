package com.nc.airport.backend.persistence.eav.mutable2query;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.sql.*;

@Log4j2
class ServiceDBFetcher {
    private Connection connection;

    ServiceDBFetcher(Connection connection) {
        this.connection = connection;
    }

     boolean existsByObjId(BigInteger objectId) {
        if (objectId == null)
            return false;

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM OBJECTS WHERE OBJECT_ID = ?"
            );
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Couldn't prepare the statement", e);
        }

        setIdentificator(statement, objectId);

        try {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            return true;
        }
    }

    BigInteger countById(BigInteger objTypeId) {
        if (objTypeId == null)
            return new BigInteger("0");

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(
                    "SELECT COUNT(OBJECT_ID) FROM OBJECTS WHERE OBJECT_TYPE_ID = ?"
            );
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Couldn't prepare the statement", e);
        }

        setIdentificator(statement, objTypeId);

        try {
            ResultSet result = statement.executeQuery();
            result.next();
            return new BigInteger(result.getString(1));
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Error occurred after query execution", e);
        }
    }

    private void setIdentificator(PreparedStatement statement, BigInteger identificator) {
        try {
            statement.setObject(1, identificator);
        } catch (SQLException e) {
            log.error(e);
            throw new BadDBRequestException("Couldn't set the identificator" + identificator, e);
        }
    }

}
