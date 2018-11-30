package com.nc.airport.backend.model.dto;

import com.nc.airport.backend.model.entities.User;

import java.util.List;

public class UserFilteringWrapper {
    List<User> users;
    int countOfPages;

    public UserFilteringWrapper(List<User> users, int countOfPages) {
        this.users = users;
        this.countOfPages = countOfPages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(int countOfPages) {
        this.countOfPages = countOfPages;
    }
}
