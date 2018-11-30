package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UsersRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
//    @Query(value = "select * from \"USER\" inner join AUTHORITY on \"USER\".AUTHORITY_ID = AUTHORITY.ID", nativeQuery = true)
    @Query(value = "select * from USERS inner join AUTHORITY on USERS.AUTHORITY_ID = AUTHORITY.ID", nativeQuery = true)
    List<User> getAll();

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    User findUserByEmailAndPassword(String email, String password);
}
