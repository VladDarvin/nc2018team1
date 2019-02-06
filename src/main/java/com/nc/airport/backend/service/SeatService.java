package com.nc.airport.backend.service;

import com.nc.airport.backend.model.entities.model.airplane.Airplane;
import com.nc.airport.backend.model.entities.model.airplane.Seat;
import com.nc.airport.backend.model.entities.model.airplane.SeatType;
import com.nc.airport.backend.model.entities.model.airplane.dto.AirplaneDto;
import com.nc.airport.backend.model.entities.model.airplane.dto.SeatDto;
import com.nc.airport.backend.persistence.eav.repository.EavCrudRepository;
import com.nc.airport.backend.service.exception.InconsistencyException;
import com.nc.airport.backend.service.exception.ItemNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@Log4j2
public class SeatService extends AbstractService<Seat> {

    private final SeatTypeService seatTypeService;
    private final AirplaneService airplaneService;

    @Autowired
    public SeatService(EavCrudRepository<Seat> repository, SeatTypeService seatTypeService, AirplaneService airplaneService) {
        super(Seat.class, repository);
        this.seatTypeService = seatTypeService;
        this.airplaneService = airplaneService;
    }

    public List<SeatDto> getByPlaneId(BigInteger id) {
        List<Seat> seats = repository.findSliceOfReference(id, Seat.class);
        List<SeatDto> seatDtos = seatsToSeatDtos(id, seats);
        log.debug(seatDtos);
        return seatDtos;
    }

    /**
     *
     * @param planeObjId can be null, in this case plane id is extracted from the first seat in seat list
     * @param seats
     * @return
     */
    public List<SeatDto> seatsToSeatDtos(BigInteger planeObjId, List<Seat> seats) {
        if (planeObjId == null) {
            if (seats != null && seats.size() > 0) {
                planeObjId = seats.get(0).getAirplaneId();
            } else {
//                FIXME throw convenient exception
                throw new RuntimeException("No plane id in arg list. " +
                        "No plane id in the first seat of seats list. " +
                        "Can't convert seats to seatDtos.");
            }
        }

        Set<BigInteger> seatTypeIdSet = gatherSeatTypeIds(seats);
        Map<BigInteger, SeatType> idToSeatType = getIdToSeatType(seatTypeIdSet);
        AirplaneDto airplaneDto = new AirplaneDto(airplaneService.getByObjectId(planeObjId));

        List<SeatDto> seatDtos = new ArrayList<>();
        for (Seat seat : seats) {
//            alas, for normal functionality front-end needs seatType name filled in seats :(
//            FIXME fix front-end to get seatTypes from backend, not from seats payload
            SeatType seatType = idToSeatType.get(seat.getSeatTypeId());
            SeatDto seatDto = new SeatDto(seat, seatType);
            seatDto.setAirplane(airplaneDto);
            seatDtos.add(seatDto);
        }
        return seatDtos;
    }


    private Map<BigInteger, SeatType> getIdToSeatType(Set<BigInteger> seatTypeIdSet) {
        //        THIS IS TO REDUCE THE NUMBER OF TIMES WE TALK TO DB
        Map<BigInteger, SeatType> idToSeatTypeMap = new HashMap<>();
        for (BigInteger id : seatTypeIdSet) {
            Optional<SeatType> result = seatTypeService.getSeatTypeById(id);
            if (result.isPresent()) {
                idToSeatTypeMap.put(id, result.get());
            } else {
                logAndThrow(new ItemNotFoundException("Cannot find seat type of id " + id));
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

    public List<SeatDto> saveAll(List<SeatDto> seats, BigInteger planeId) {
        if (seats == null || seats.isEmpty() || planeId == null) {
            log.warn("When saving seats got bad arguments: seats={}, planeId={}", seats, planeId);
            return new ArrayList<>();
        }

        checkSeatsAreConsistent(seats);

        AirplaneDto airplane = seats.get(0).getAirplane();
        checkPlaneIsUpToDate(airplane);

        airplane.increaseVersion();
        airplaneService.updateEntity(new Airplane(airplane));

        deletePlaneSeats(planeId);

//        insert all the seats from query
        List<SeatDto> updatedSeats = new ArrayList<>();
        for (SeatDto seat : seats) {
            SeatDto updatedSeat = new SeatDto(repository.update(new Seat(seat)));
            updatedSeat.setAirplane(airplane);
            updatedSeats.add(updatedSeat);
        }
        return updatedSeats;
    }

    private void checkPlaneIsUpToDate(AirplaneDto airplane) {
        boolean planeIsUpToDate = airplaneService.ifPlaneVersionUpToDate(airplane.getObjectId(), airplane.getVersionNum());
        if (!planeIsUpToDate) {
            throw new InconsistencyException("Plane version is outdated. Reload the plane to get the latest version.");
        }
    }

    private void checkSeatsAreConsistent(List<SeatDto> seats) {
        for (SeatDto seat : seats) {
            if (seat == null) {
                throw new RuntimeException("malformed seat, is null");
            }
            if (seat.getAirplane().getObjectId() == null) {
                throw new RuntimeException("malformed seat, no airplane id");
            }
            if (seat.getSeatType().getObjectId() == null) {
                throw new RuntimeException("malformed seat, no seatType id");
            }
            if (seat.getAirplane().getVersionNum() == null) {
                throw new RuntimeException("malformed seat, no airplane version");
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

    public Seat getByObjectId(BigInteger objId) {
        Optional<Seat> result = repository.findById(objId, Seat.class);
        if (result.isPresent()) {
            return result.get();
        } else {
            String message = "Tried to find seat of id=" + objId + " but it does not exist.";
            InconsistencyException ex = new InconsistencyException(message);
            logAndThrow(ex);
        }
        return null;
    }
}
