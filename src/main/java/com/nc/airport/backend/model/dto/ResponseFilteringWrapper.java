package com.nc.airport.backend.model.dto;

import java.math.BigInteger;
import java.util.List;

public class ResponseFilteringWrapper {
    List<Object> entities;
    BigInteger countOfPages;

    public ResponseFilteringWrapper(List<Object> entities, BigInteger countOfPages) {
        this.entities = entities;
        this.countOfPages = countOfPages;
    }

    public List<Object> getEntities() {
        return entities;
    }

    public void setEntities(List<Object> entities) {
        this.entities = entities;
    }

    public BigInteger getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(BigInteger countOfPages) {
        this.countOfPages = countOfPages;
    }
}
