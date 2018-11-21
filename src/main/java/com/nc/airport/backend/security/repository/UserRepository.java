package com.nc.airport.backend.security.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by stephan on 20.03.16.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
