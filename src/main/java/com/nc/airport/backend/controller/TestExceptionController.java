package com.nc.airport.backend.controller;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.exceptions.InvalidAnnotatedClassException;
import com.nc.airport.backend.security.controller.AuthenticationException;
import com.nc.airport.backend.service.exception.InconsistencyException;
import com.nc.airport.backend.service.exception.ItemNotFoundException;
import com.nc.airport.backend.service.exception.PersistenceException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/error")
public class TestExceptionController {

    private List<RuntimeException> exceptions = new ArrayList<>();
    private Map<Integer, String> idToName = new HashMap<>();

    public TestExceptionController() {
        exceptions.add(new PersistenceException("persistence message", null));
        exceptions.add(new EntityExistsException("entity already exists message"));
        exceptions.add(new EntityNotFoundException("entity was not found message"));
        exceptions.add(new BadDBRequestException("bad db request message", null));
        exceptions.add(new DatabaseConnectionException("database connection exception message", null));
        exceptions.add(new InvalidAnnotatedClassException("invalid annotated class message", null));
        exceptions.add(new InconsistencyException("inconsistency message"));
        exceptions.add(new AuthenticationException("auth exception message", null));
        exceptions.add(new ItemNotFoundException("item hasn't been found message"));

        fillMap();
    }

    private void fillMap() {
        int i = 0;
        for (RuntimeException exception : exceptions) {
            i++;
            idToName.put(i, exception.getClass().getSimpleName());
        }
    }

    @GetMapping("/{id}")
    public void throwException(@PathVariable int id) {
        id--;
        if (exceptions.size() > id) {
            throw exceptions.get(id);
        }
    }

    @GetMapping
    public Map<Integer, String> getAll() {
        return idToName;
    }
}
