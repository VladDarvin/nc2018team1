package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
//    TODO add register-user

    public UserController(UserService service) {
        this.service = service;
    }

    private UserService service;

    @GetMapping("/page={page}")
    public List<User> getTenUsers(@PathVariable(name = "page") int page) {
        return service.getTenEntities(page);
    }

    @PostMapping
    public User addNewUser(@RequestBody User entity) {
        return service.saveEntity(entity);
    }

    @PutMapping
    public User editUser(@RequestBody User entity) {
        return service.saveEntity(entity);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") BigInteger id) {
        service.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfUsers() {
        return service.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfUsersByFilter(@PathVariable(name = "searchString") String searchString) {
        return service.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchUsers(@PathVariable(name = "page") int page,
                                                    @RequestBody SortingFilteringWrapper wrapper) {
        return service.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
