package com.nc.airport.backend.eav.dao;

import java.sql.Connection;

import com.nc.airport.backend.eav.mutable.Mutable;

import java.sql.SQLException;

public class Mutable2Query {
    private Connection connection;

    public Mutable2Query(Connection connection) throws SQLException{
        this.connection = connection;
    }

    public void sqlInsert (Mutable mutable) throws SQLException {
        buildASequence(new InsertSequenceBuilder(connection), mutable);
    }

    public void sqlUpdate(Mutable mutable) throws SQLException{
        buildASequence(new UpdateSequenceBuilder(connection), mutable);
    }

    public void sqlDelete(Mutable mutable){
        buildASequence(new DeleteSequenceBuilder(connection), mutable);
    }

    private void buildASequence(SequenceBuilder sequenceBuilder, Mutable mutable){
        sequenceBuilder.build(mutable);
    }
}
