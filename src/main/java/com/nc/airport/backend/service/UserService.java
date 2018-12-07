package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.UserDTO;
import com.nc.airport.backend.model.dto.UserFilteringWrapper;
import com.nc.airport.backend.model.entities.User;
import com.nc.airport.backend.repository.UserFilter;
import com.nc.airport.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return usersRepository.save(user);
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

    public UserFilteringWrapper search(List<Map<String, Object>> criterias, int page) {
        UserFilter filter =
                new UserFilter(criterias);
        Page<User> pageOfUsers = usersRepository.findAll(filter, PageRequest.of(page - 1, 10));
        return new UserFilteringWrapper(pageOfUsers.getContent(), pageOfUsers.getTotalPages());
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
        } else if (field.equals("phonenumber")) {
            return usersRepository.findAllByOrderByPhonenumberAsc();
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
        } else if (field.equals("phonenumber")) {
            return usersRepository.findAllByOrderByPhonenumberDesc();
        }
        return null;
    }

}
