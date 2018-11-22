package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UsersRepository extends PagingAndSortingRepository<User, Long> {
    User findUserByEmailAndPassword(String email, String password);
    User findByUsername(String username);
}
