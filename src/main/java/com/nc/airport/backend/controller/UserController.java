package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.UserFilteringWrapper;
import com.nc.airport.backend.model.entities.Authority;
import com.nc.airport.backend.model.entities.User;
import com.nc.airport.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @RequestMapping(value = "/registrate", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:4200")
    public User registrateNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:4200")
    public User addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "http://localhost:4200")
    public User editUser(@PathVariable(name = "id") long id, @RequestBody User user) {
        user.setId(id);
        return userService.editUser(user);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteUser(@PathVariable(name = "id") long id) {
        userService.deleteUser(id);
    }

    @RequestMapping(value = "/users/page={page}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public List<User> getTenUsers(@PathVariable(name = "page") int page) {
        return userService.getTenUsers(page);
    }

    @RequestMapping(value = "/users/search/page={page}", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:4200")
    public UserFilteringWrapper searchUsers(@PathVariable(name = "page") int page, @RequestBody List<Map<String, Object>> criterias) {
        return userService.search(criterias, page);
    }

    @RequestMapping(value = "/users/count", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public Long getCountOfUsers() {
        return userService.getUsersAmount();
    }

    @RequestMapping(value = "/users/sortAscBy={field}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public List<User> sortUserAscBy(@PathVariable(name = "field") String field) {
        return userService.sortUsersByFieldAsc(field);
    }

    @RequestMapping(value = "/users/sortDescBy={field}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:4200")
    public List<User> sortUserDescBy(@PathVariable(name = "field") String field) {
        return userService.sortUsersByFieldDesc(field);
    }

}
