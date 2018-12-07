package com.nc.airport.backend.security.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    //    @Query(value = "select * from USERS where EMAIL = :email", nativeQuery = true)
    User findByEmail(@Param("email") String email);
}
