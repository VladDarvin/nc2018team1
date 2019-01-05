package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airline.Airline;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

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
        return airlinesRepository.findSlice(Airline.class, new Page(page - 1));
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

    public BigInteger getAmountOfFilteredAirlines(String searchString) {
        List<FilterEntity> filterBy = makeFilterList(searchString);
        return airlinesRepository.count(Airline.class, filterBy);
    }

    public List<Airline> filterAndSortAirlines(int page, String search, List<SortEntity> sortEntities) {
        List<FilterEntity> filterEntities = makeFilterList(search);
        return airlinesRepository.findSlice(Airline.class, new Page(page - 1), sortEntities, filterEntities);

    }

    private List<FilterEntity> makeFilterList(String search) {
        String searchString = "%" + search + "%";
        List<BigInteger> attributeIds = ReflectionHelper.getAttributeIds(Airline.class);
        Map<BigInteger, Set<Object>> filtering = new HashMap<>();
        for (BigInteger id :
                attributeIds) {
            filtering.put(id, new HashSet<>(Arrays.asList(searchString)));
        }

        List<FilterEntity> filterEntities = null;
        filterEntities = new ArrayList<>();
        for (BigInteger key :
                filtering.keySet()) {
            filterEntities.add(new FilterEntity(key, filtering.get(key)));
        }
        return filterEntities;
    }
}
