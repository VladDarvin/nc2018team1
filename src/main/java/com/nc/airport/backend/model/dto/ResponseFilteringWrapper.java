package com.nc.airport.backend.model.dto;

import java.math.BigInteger;
import java.util.List;

public class ResponseFilteringWrapper<T> {
    private List<T> entities;
    private BigInteger countOfPages;

    public ResponseFilteringWrapper(List<T> entities, BigInteger countOfPages) {
        this.entities = entities;
        this.countOfPages = countOfPages;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public BigInteger getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(BigInteger countOfPages) {
        this.countOfPages = countOfPages;
    }
}
