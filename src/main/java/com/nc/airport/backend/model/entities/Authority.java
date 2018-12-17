package com.nc.airport.backend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Authority implements GrantedAuthority {

    @Id
    @SequenceGenerator(name="authority_id",sequenceName="authority_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="authority_id")
    private Long id;

    private String name;

    public Authority() {
    }

    public Authority(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Authority(String name) {
        this.name = name;
    }

//    @JsonIgnore
//    @OneToMany(mappedBy="authority")
//    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
}
