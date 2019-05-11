package com.nc.airport.backend.controller;

import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.dto.SortingFilteringWrapper;
import com.nc.airport.backend.model.entities.model.airplane.ExtraType;
import com.nc.airport.backend.service.ExtraTypeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("extra-type")
public class ExtraTypeController {
    private ExtraTypeService extraTypeService;

    public ExtraTypeController(ExtraTypeService extraTypeService) {
        this.extraTypeService = extraTypeService;
    }

    @GetMapping("/page={page}")
    public List<ExtraType> getTenExtraTypes(@PathVariable(name = "page") int page) {
        return extraTypeService.getTenEntities(page);
    }

    @PostMapping
    public ExtraType addNewExtraType(@RequestBody ExtraType airplane) {
        return extraTypeService.updateEntity(airplane);
    }

    @PutMapping
    public ExtraType editExtraType(@RequestBody ExtraType airplane) {
        return extraTypeService.updateEntity(airplane);
    }

    @DeleteMapping("/{id}")
    public void deleteExtraType(@PathVariable(name = "id") BigInteger id) {
        extraTypeService.deleteEntity(id);
    }

    @GetMapping("/count")
    public BigInteger getCountOfExtraTypes() {
        return extraTypeService.getEntitiesAmount();
    }

    @GetMapping("/count/search={searchString}")
    public BigInteger getCountOfExtraTypesByFilter(@PathVariable(name = "searchString") String searchString) {
        return extraTypeService.getAmountOfFilteredEntities(searchString);
    }

    @PostMapping("/search/page={page}")
    public ResponseFilteringWrapper searchExtraTypes(@PathVariable(name = "page") int page,
                                                     @RequestBody SortingFilteringWrapper wrapper) {
        return extraTypeService.filterAndSortEntities(page, wrapper.getSearchString(), wrapper.getSortList());
    }
}
