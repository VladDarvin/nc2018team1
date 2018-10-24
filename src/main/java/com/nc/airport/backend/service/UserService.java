package com.nc.airport.backend.service;

import com.nc.airport.backend.model.User;
import com.nc.airport.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User editUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public List<User> getTenUsers(int page) {
        Page<User> pageOfUsers = userRepository.findAll(new PageRequest(page-1, 10));
        return pageOfUsers.getContent();
    }

}
