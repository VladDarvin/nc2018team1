package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.users.Authority;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RegistrationController {
    private UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setAuthority(Authority.ROLE_USER);
//        TODO IMPLEMENT PROPERLY
        user.setEnabled(true);
        return service.saveNewUser(user);
    }
}
