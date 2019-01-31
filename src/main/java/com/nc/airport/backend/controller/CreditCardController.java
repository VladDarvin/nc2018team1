package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.entities.model.users.CreditCard;
import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.service.CreditCardService;
import com.nc.airport.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class CreditCardController {
    private CreditCardService service;
    private UserService userService;

    public CreditCardController(CreditCardService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/userLogin={userLogin}/page={page}")
    public List<CreditCard> getTenCreditCards(@PathVariable String userLogin, @PathVariable(name = "page") int page) {
        return service.findCreditCardsByUserLogin(userLogin, page);
    }

    @PostMapping("/userLogin={userLogin}")
    public CreditCard addNewCreditCard(@PathVariable String userLogin, @RequestBody CreditCard entity) {
        User user = userService.findByLogin(userLogin);
        entity.setAuthorizedUserId(user.getObjectId());
        return service.updateEntity(entity);
    }

    @DeleteMapping("/{id}")
    public void deleteCreditCard(@PathVariable(name = "id") BigInteger id) {
        service.deleteEntity(id);
    }

}
