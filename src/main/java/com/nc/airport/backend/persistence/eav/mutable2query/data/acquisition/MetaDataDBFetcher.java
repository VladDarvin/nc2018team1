package com.nc.airport.backend.persistence.eav.mutable2query.data.acquisition;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class MetaDataDBFetcher {
    private Connection connection;

    public MetaDataDBFetcher(Connection connection) {
        this.connection = connection;
    }

    public boolean existsByObjId(BigInteger objectId) {
        if (objectId == null)
            return false;

        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM OBJECTS WHERE OBJECT_ID = ?")) {

            setIdentificator(statement, objectId);
            try {
                return statement.executeUpdate() != 0;
            } catch (SQLException e) {
                return true;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Couldn't prepare the statement", e);
        }
    }

    public BigInteger countById(BigInteger objTypeId) {
        if (objTypeId == null)
            return new BigInteger("0");

        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT COUNT(OBJECT_ID) FROM OBJECTS WHERE OBJECT_TYPE_ID = ?")) {

            setIdentificator(statement, objTypeId);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                BigInteger number = new BigInteger(result.getString(1));
                result.close();
                statement.close();
                return number;
            } catch (SQLException e) {
                log.error(e);
                throw new BadDBRequestException("Error occurred after query execution", e);
            }

        } catch (SQLException e) {
            log.error(e);
            throw new DatabaseConnectionException("Couldn't prepare the statement", e);
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
