package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import com.nc.airport.backend.service.exception.PersistenceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class UserService extends AbstractService<User> {

    public UserService(EavCrudRepository<User> repository) {
        super(User.class, repository);
    }

    /**
     * Returns a user with given login, or null if nothing found
     * <h2>ATTR ID MUST BE 44 :(</h2>
     *
     * @param login search criteria
     * @return null if login is not found
     */
    public User findByLogin(String login) {
        BigInteger loginAttrId = new BigInteger("44");
        return findUserByAttr(login, loginAttrId);
    }

    /**
     * Returns a user with given email, or null if nothing found
     * <h2>ATTR ID MUST BE 46 :(</h2>
     *
     * @param email search criteria
     * @return null if email is not found
     */
    public User findByEmail(String email) {
        BigInteger emailAttrId = new BigInteger("46");
        return findUserByAttr(email, emailAttrId);
    }

    private User findUserByAttr(String value, BigInteger attrId) {
        User foundUser;
        try {
            foundUser = findByAttr(value, attrId, User.class);
        } catch (IllegalStateException e) {
            String message = "Found more than 1 user with the same unique attribute(id=" + attrId + "): " + value;
            IllegalStateException exception = new IllegalStateException(message);
            log.error(message, exception);
            throw exception;
        }

        return foundUser;
    }

    /**
     * Checks that login and email are unique
     *
     * @param entity saved entity
     * @return updated user
     */
    public User saveNewUser(User entity) {
        if (findByLogin(entity.getLogin()) != null) {
            throw new PersistenceException("User with this login already exists", entity);
        }
        if (findByEmail(entity.getEmail()) != null) {
            throw new PersistenceException("User with this email already exists", entity);
        }

//        TODO IMPLEMENT ENABLING ACCOUNT
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        entity.setEnabled(true);
        return super.saveEntity(entity);
    }
}
