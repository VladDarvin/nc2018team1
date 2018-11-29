package com.nc.airport.backend.security.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
