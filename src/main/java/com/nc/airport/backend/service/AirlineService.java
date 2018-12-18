package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AirlineService {
    private EavCrudRepository<Airline> airlinesRepository;

    @Autowired
    public AirlineService(EavCrudRepository<Airline> airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

    public List<Airline> findAllAirlines() {
//         TODO USE FINDSLICE
        return airlinesRepository.findAll(Airline.class);
    }

    public List<Airline> getTenAirlines(int page) {
        int offset = (page - 1) * 10;
        return airlinesRepository.findSlice(Airline.class, 1 + offset, 10 + offset);
    }

    public Airline addAirline(Airline airline) {
        return airlinesRepository.save(airline);
    }

    public void deleteAirline(BigInteger id) {
        airlinesRepository.deleteById(id);
    }

    public BigInteger getAirlinesAmount() {
        return airlinesRepository.count(Airline.class);
    }

    public List<Airline> filtrateAndSortAirlines(int page,
                                                 Map<BigInteger, Set<Object>> filtering, Map<BigInteger, Boolean> sorting) {
        List<FilterEntity> filterEntities = null;
        if (filtering != null) {
            filterEntities = new ArrayList<>();
            for (BigInteger key:
                    filtering.keySet()) {
                filterEntities.add(new FilterEntity(key, filtering.get(key)));
            }
        }

        List<SortEntity> sortEntities = null;
        if (sorting != null) {
            sortEntities = new ArrayList<>();
            for (BigInteger key:
                    sorting.keySet()) {
                sortEntities.add(new SortEntity(key, sorting.get(key)));
            }
        }

        return airlinesRepository.findSlice(Airline.class, page*10, page*10+10, sortEntities, filterEntities);

    }
}
