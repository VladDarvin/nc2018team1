package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class PassengerService extends AbstractService<Passenger> {

    private UserService userService;

    public PassengerService(EavCrudRepository<Passenger> repository, UserService userService) {
        super(Passenger.class, repository);
        this.userService = userService;
    }

    public List<Passenger> findAllPassengersByUserLogin(String userLogin, int page) {
        User parentUser = userService.findByLogin(userLogin);
        return repository.findSliceOfChildren(parentUser.getObjectId(), Passenger.class, new Page(page - 1));
    }

    public ResponseFilteringWrapper filterAndSortEntitiesByUserLogin(String userLogin, int page, String searchRequest, List<SortEntity> sortEntities) {
        User parentUser = userService.findByLogin(userLogin);
        List<FilterEntity> filterEntities = makeFilterList(searchRequest, Passenger.class);
        List<Passenger> foundEntities = repository.findSliceOfChildren(parentUser.getObjectId(), Passenger.class, new Page(page - 1), sortEntities, filterEntities);
        BigInteger countOfPages = repository.count(Passenger.class, filterEntities);
        return new ResponseFilteringWrapper<>(foundEntities, countOfPages);
    }

}
