package com.nc.airport.backend.service;

import com.nc.airport.backend.model.dto.AirportDto;
import com.nc.airport.backend.model.dto.ResponseFilteringWrapper;
import com.nc.airport.backend.model.entities.model.flight.Airport;
import com.nc.airport.backend.model.entities.model.flight.Country;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirportService extends AbstractService<Airport> {
    private CountryService countryService;

    public AirportService(EavCrudRepository<Airport> repository, CountryService countryService) {
        super(Airport.class, repository);
        this.countryService = countryService;
    }

    public List<Airport> getAll() {
        int quantity = repository.count(Airport.class).intValue();
        Page page = new Page(quantity, 0);
        return repository.findSlice(Airport.class, page);
    }

    public List<AirportDto> getTenDtoEntities(int page) {
        List<Airport> plainAirports = super.getTenEntities(page);
        return makeAirportDtos(plainAirports);
    }

    public ResponseFilteringWrapper getDtoBySearchingResult(ResponseFilteringWrapper wrapper) {
        List<Airport> plainAirports = wrapper.getEntities();
        List<AirportDto> airportDtos = makeAirportDtos(plainAirports);
        wrapper.setEntities(airportDtos);
        return wrapper;
    }

    private List<AirportDto> makeAirportDtos(List<Airport> plainAirports) {
        List<AirportDto> airportDtos = new ArrayList<>();
        for (Airport airport : plainAirports) {
            BigInteger countryId = airport.getCountry();
            Country country = countryService.findCountryByObjectId(countryId);
            AirportDto newAirportDto = new AirportDto(airport, country);
            airportDtos.add(newAirportDto);
        }
        return airportDtos;
    }
}
