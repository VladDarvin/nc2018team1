package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.UserDTO;
import com.nc.airport.backend.model.entities.Authority;
import com.nc.airport.backend.model.entities.User;
import com.nc.airport.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getUsers() {
        return usersRepository.getAll();
        //return (List<User>) usersRepository.findAll();
    }

    public User addUser(User user) {
        User existUser = usersRepository.findUserByEmail(user.getEmail());
        if (existUser == null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            if (user.getEnabled() == null) {
                user.setEnabled(true);
            }
            if (user.getAuthority() == null) {
                user.setAuthority(new Authority(2L, "ROLE_USER"));
            }
            return usersRepository.save(user);
        } else {
            throw new EntityExistsException("User with this email already exists");
        }
    }

    public User logIn(UserDTO user) {
        return usersRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public User editUser(User user) {
        return usersRepository.save(user);
    }

    public void deleteUser(long id) {
        usersRepository.deleteById(id);
    }

    public List<User> getTenUsers(int page) {
        Page<User> pageOfUsers = usersRepository.findAll(PageRequest.of(page - 1, 10));
        return pageOfUsers.getContent();
    }

    public ResponseFilteringWrapper search(String searchString, int page) {
        Page<User> pageOfUsers = usersRepository.findAll(searchString, PageRequest.of(page - 1, 10));
        List<Object> entities = new ArrayList<>(pageOfUsers.getContent());
        return new ResponseFilteringWrapper(entities, BigInteger.valueOf(pageOfUsers.getTotalPages()));
    }

    public Long getUsersAmount() {
        return usersRepository.count();
    }

    public List<User> sortUsersByFieldAsc(String field) {
        if (field.equals("firstname")) {
            return usersRepository.findAllByOrderByFirstnameAsc();
        } else if (field.equals("lastname")) {
            return usersRepository.findAllByOrderByLastnameAsc();
        } else if (field.equals("email")) {
            return usersRepository.findAllByOrderByEmailAsc();
        } else if (field.equals("phoneNumber")) {
            return usersRepository.findAllByOrderByPhoneNumberAsc();
        }
        return null;
    }

    public List<User> sortUsersByFieldDesc(String field) {
        if (field.equals("firstname")) {
            return usersRepository.findAllByOrderByFirstnameDesc();
        } else if (field.equals("lastname")) {
            return usersRepository.findAllByOrderByLastnameDesc();
        } else if (field.equals("email")) {
            return usersRepository.findAllByOrderByEmailDesc();
        } else if (field.equals("phoneNumber")) {
            return usersRepository.findAllByOrderByPhoneNumberDesc();
        }
        return null;
    }

}
