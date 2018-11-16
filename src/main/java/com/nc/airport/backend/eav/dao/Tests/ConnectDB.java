package com.nc.airport.backend.eav.dao.Tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;

/**
 * Created by User on 16.11.2018.
 */
public class ConnectDB{

    final String DB_URL= "jdbc:oracle:thin:@localhost:1521:XE";
    final String DB_USER = "TESTING_DAO";
    final String DB_PASSWORD = "123";


    public Connection makeConnection() throws SQLException {
        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");
        info.put(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES,
                "(MD5,SHA1,SHA256,SHA384,SHA512)");
        info.put(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CHECKSUM_LEVEL,
                "REQUIRED");

        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        OracleConnection connection = (OracleConnection) ods.getConnection();
        return connection;
    }
}
