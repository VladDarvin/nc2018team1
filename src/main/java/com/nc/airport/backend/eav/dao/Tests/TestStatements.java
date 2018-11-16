package com.nc.airport.backend.eav.dao.Tests;

import com.nc.airport.backend.eav.dao.Mutable2Query;
import com.nc.airport.backend.eav.mutable.Mutable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by User on 16.11.2018.
 */
public class TestStatements {
    public static void main(String []arg) throws SQLException{
        Connection con = new ConnectDB().makeConnection();
        MutableFactory factory = new MutableFactory();
        Mutable mutable = factory.createMutable();
        Mutable2Query service = new Mutable2Query(con);
        service.sqlInsert(mutable);

        mutable = factory.createMutableForUpdate();
        service.sqlUpdate(mutable);

        service.sqlDelete(mutable);
    }
}
