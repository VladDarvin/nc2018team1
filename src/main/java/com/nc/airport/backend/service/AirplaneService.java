package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.persistence.eav.entity2mutable.util.ReflectionHelper;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class AirplaneService {
    private EavCrudRepository<Airplane> airplaneRepository;

    @Autowired
    public AirplaneService(EavCrudRepository<Airplane> airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<Airplane> findAllAirplanes() {
        return airplaneRepository.findAll(Airplane.class);
    }

    public List<Airplane> getTenAirplanes(int page) {
        int offset = (page - 1) * 10;
        return airplaneRepository.findSlice(Airplane.class, 1 + offset, 10 + offset);
    }

    public Airplane addAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public void deleteAirplane(BigInteger id) {
        airplaneRepository.deleteById(id);
    }

    public BigInteger getAirplanesAmount() {
        return airplaneRepository.count(Airplane.class);
    }

    public BigInteger getAmountOfFiltrateAirplanes(String searchString) {
        List<FilterEntity> filterBy = makeFilterList(searchString);
        return airplaneRepository.count(Airplane.class, filterBy);
    }

    public List<Airplane> filterAndSortAirlines(int page, String search, List<SortEntity> sortEntities) {

        List<FilterEntity> filterEntities = makeFilterList(search);

        int offset = (page - 1) * 10;
        return airplaneRepository.findSlice(Airplane.class, 1 + offset, 10 + offset, sortEntities, filterEntities);

    }

    private List<FilterEntity> makeFilterList(String search) {
        String searchString = "%"+search+"%";
        List<BigInteger> attributeIds = ReflectionHelper.getAttributeIds(Airplane.class);
        Map<BigInteger, Set<Object>> filtering = new HashMap<>();
        for (BigInteger id:
                attributeIds) {
            filtering.put(id, new HashSet<>(Arrays.asList(searchString)));
        }

        List<FilterEntity> filterEntities = null;
        if (searchString != null) {
            filterEntities = new ArrayList<>();
            for (BigInteger key:
                    filtering.keySet()) {
                filterEntities.add(new FilterEntity(key, filtering.get(key)));
            }
        }
        return filterEntities;
    }
}

