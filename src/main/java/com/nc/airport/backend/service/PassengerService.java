package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.PassengerPassportDTO;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passenger;
import com.nc.airport.backend.model.entities.model.ticketinfo.Passport;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.sorting.SortEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService extends AbstractService<Passenger> {

    private UserService userService;
    private PassportService passportService;

    public PassengerService(EavCrudRepository<Passenger> repository, UserService userService, PassportService passportService) {
        super(Passenger.class, repository);
        this.userService = userService;
        this.passportService = passportService;
    }

    public List<Passenger> findAllPassengersByUserLogin(String userLogin, Page page) {
        User parentUser = userService.findByLogin(userLogin);
        return repository.findSliceOfChildren(parentUser.getObjectId(), Passenger.class, page);
    }

    public ResponseFilteringWrapper filterAndSortEntitiesByUserLogin(String userLogin, int page, String searchRequest, List<SortEntity> sortEntities) {
        User parentUser = userService.findByLogin(userLogin);
        List<FilterEntity> filterEntities = makeFilterList(searchRequest, Passenger.class);
        List<Passenger> foundEntities = repository.findSliceOfChildren(parentUser.getObjectId(), Passenger.class, new Page(page - 1), sortEntities, filterEntities);
        BigInteger countOfPages = repository.count(Passenger.class, filterEntities);
        return new ResponseFilteringWrapper<>(foundEntities, countOfPages);
    }

    public List<PassengerPassportDTO> getAllByUserLogin(String userLogin) {
//        ne sudite strogo, tut do menya nagovnokodili, u menya ruki opuskayutsya
        List<Passenger> passengers = findAllPassengersByUserLogin(userLogin, new Page(50, 0));

        List<PassengerPassportDTO> dtos = new ArrayList<>();
        for (Passenger passenger : passengers) {
            Passport passport = passportService.findPassportByReference(passenger.getPassportId());
            dtos.add(new PassengerPassportDTO(passenger, passport));
        }

        return dtos;
    }
}
