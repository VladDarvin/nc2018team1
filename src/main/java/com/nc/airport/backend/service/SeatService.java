package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.persistence.eav.mutable2query.filtering2sorting.filtering.FilterEntity;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.persistence.eav.repository.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class SeatService extends AbstractService<Seat> {

    public SeatService(EavCrudRepository<Seat> repository) {
        super(Seat.class, repository);
    }

    /**
     * <h3>WARNING</h3>
     * AIRPLANE_ID must be 23!
     *
     * @param id
     * @return
     */
    public List<Seat> getByPlaneId(BigInteger id) {
        HashSet<Object> filterSet = new HashSet<>();
        filterSet.add(id);
        FilterEntity filter = new FilterEntity(BigInteger.valueOf(23), filterSet);
        List<FilterEntity> filterList = Collections.singletonList(filter);

        Page maxSizePage = new Page(repository.count(Seat.class, filterList).intValue(), 0);
        return repository.findSlice(Seat.class, maxSizePage, new ArrayList<>(), filterList);
    }
}
