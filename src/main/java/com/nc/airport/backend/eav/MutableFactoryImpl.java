package com.nc.airport.backend.eav;

public class MutableFactoryImpl implements MutableFactory {
    @Override
    public Mutable getNewMutable() {
        return new Mutable();
    }
}
