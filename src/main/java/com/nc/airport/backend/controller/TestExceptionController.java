package com.nc.airport.backend.controller;

import com.nc.airport.backend.persistence.eav.exceptions.BadDBRequestException;
import com.nc.airport.backend.persistence.eav.exceptions.DatabaseConnectionException;
import com.nc.airport.backend.persistence.eav.exceptions.InvalidAnnotatedClassException;
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
        exceptions.add(new PersistenceException("msg1", null));
        exceptions.add(new EntityExistsException("msg1"));
        exceptions.add(new EntityNotFoundException("msg1"));
        exceptions.add(new BadDBRequestException("msg1", null));
        exceptions.add(new DatabaseConnectionException("msg1", null));
        exceptions.add(new InvalidAnnotatedClassException("msg1", null));

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
        if (exceptions.size() > id) {
            throw exceptions.get(id);
        }
    }

    @GetMapping
    public Map<Integer, String> getAll(){
        return idToName;
    }
}
