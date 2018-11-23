package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UsersRepository extends PagingAndSortingRepository<User, Long> {
    @Query(value = "select * from user join user_authority on user.id = user_authority.user_id join authority on user_authority.authority_id = authority.id",
    nativeQuery = true)
    List<User> getAll();
    @Query(value = "insert into user join user_authority on user.id = user_authority.user_id join authority on user_authority.authority_id = authority.id",
            nativeQuery = true)
    User addNewUser(User user);
    User findUserByEmailAndPassword(String email, String password);
    User findByUsername(String username);
}
