package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.users.CreditCard;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService extends AbstractService<CreditCard> {

    private UserService userService;

    public CreditCardService(EavCrudRepository<CreditCard> repository, UserService userService) {
        super(CreditCard.class, repository);
        this.userService = userService;
    }

    public List<CreditCard> findCreditCardsByUserLogin(String userLogin, int page) {
        User parentUser = userService.findByLogin(userLogin);
        return repository.findSliceOfReference(parentUser.getObjectId(), CreditCard.class);
    }

}
