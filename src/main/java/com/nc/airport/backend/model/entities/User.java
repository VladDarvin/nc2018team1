package com.nc.airport.backend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(generator = "user_id")
    @SequenceGenerator(name = "user_id", sequenceName = "SEQ__USERS_ID",
            initialValue = 50, allocationSize = 1)
    private Long objectId;
    @Transient
    @JsonIgnore
    private String username;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private String password;
    private Boolean enabled;
    @ManyToOne
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    public User() {
    }

    public User(String firstname, String lastname, String phoneNumber, String email) {
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(String firstname, String lastname, String phoneNumber, String email, String password, Boolean enabled) {
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String firstname, String lastname, String phoneNumber, String email, String password, Boolean enabled, Authority authority) {
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
    }

}
