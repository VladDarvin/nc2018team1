package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.model.entities.model.airplane.dto.SeatDto;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.service.exception.InconsistencyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@Log4j2
public class SeatService extends AbstractService<Seat> {

    private final SeatTypeService seatTypeService;

    @Autowired
    public SeatService(EavCrudRepository<Seat> repository, SeatTypeService seatTypeService) {
        super(Seat.class, repository);
        this.seatTypeService = seatTypeService;
    }

    /**
     * <h3>WARNING</h3>
     * AIRPLANE_ID must be 23!
     */
    public List<SeatDto> getByPlaneId(BigInteger id) {
        List<Seat> seats = repository.findSliceOfReference(id, Seat.class);
        Set<BigInteger> seatTypeIdSet = gatherSeatTypeIds(seats);
        Map<BigInteger, SeatType> idToSeatType = getIdToSeatType(seatTypeIdSet);

        List<SeatDto> seatDtos = new ArrayList<>();
        for (Seat seat : seats) {
            SeatType seatType = idToSeatType.get(seat.getSeatTypeId());
            SeatDto seatDto = new SeatDto(seat);
//            alas, for normal functionality front-end needs seatType name filled in seats :(
//            FIXME fix front-end to get seatTypes from backend, not from seats payload
            seatDto.setSeatType(seatType);
            seatDtos.add(seatDto);
        }
        log.debug(seatDtos);
        return seatDtos;
    }

    private Map<BigInteger, SeatType> getIdToSeatType(Set<BigInteger> seatTypeIdSet) {
        Map<BigInteger, SeatType> idToSeatTypeMap = new HashMap<>();
        for (BigInteger id : seatTypeIdSet) {
            Optional<SeatType> result = seatTypeService.getSeatTypeById(id);
            if (result.isPresent()) {
                idToSeatTypeMap.put(id, result.get());
            } else {
                logAndThrow(new InconsistencyException("Cannot find seat type of id " + id));
            }
        }
        return idToSeatTypeMap;
    }

    private Set<BigInteger> gatherSeatTypeIds(final List<Seat> seats) {
        Set<BigInteger> seatTypes = new HashSet<>();
        for (Seat seat : seats) {
            seatTypes.add(seat.getSeatTypeId());
        }
        return seatTypes;
    }

    /*public List<Seat> getByPlaneId(BigInteger id) {
        HashSet<Object> filterSet = new HashSet<>();
        filterSet.add(id);
        FilterEntity filter = new FilterEntity(BigInteger.valueOf(23), filterSet);
        List<FilterEntity> filterList = Collections.singletonList(filter);

        Page maxSizePage = new Page(repository.count(Seat.class, filterList).intValue(), 0);
        return repository.findSlice(Seat.class, maxSizePage, new ArrayList<>(), filterList);
    }*/

    public List<SeatDto> saveAll(List<SeatDto> seats, BigInteger id) {
//        check seats
        seatsAreOk(seats);

//        delete all the seats of plane
        deletePlaneSeats(id);

//        insert all the seats from query
        List<SeatDto> updatedSeats = new ArrayList<>();
        for (SeatDto seat : seats) {
            Seat updatedSeat = repository.update(new Seat(seat));
            updatedSeats.add(new SeatDto(updatedSeat));
        }
        return updatedSeats;
    }

    private void seatsAreOk(List<SeatDto> seats) {
        for (SeatDto seat : seats) {
            if (seat.getAirplane().getObjectId() == null) {
                throw new RuntimeException("malformed seat, no airplane id");
            }
            if (seat.getSeatType().getObjectId() == null) {
                throw new RuntimeException("malformed seat, no seatType id");
            }
        }
    }

    private void deletePlaneSeats(BigInteger id) {
        List<SeatDto> existingSeats = getByPlaneId(id);
        for (SeatDto existingSeat : existingSeats) {
            repository.deleteById(existingSeat.getObjectId());
        }
    }

    private void logAndThrow(RuntimeException ex) throws RuntimeException {
        log.error(ex);
        throw ex;
    }
}
