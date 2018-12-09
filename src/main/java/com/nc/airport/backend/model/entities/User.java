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
    private Long id;
    @Transient
    @JsonIgnore
    private String username;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String email;
    private String password;
    private Boolean enabled;
    @ManyToOne
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    public User() {
    }

    public User(String firstname, String lastname, String phonenumber, String email, String password, Boolean enabled) {
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String firstname, String lastname, String phonenumber, String email, String password, Boolean enabled, Authority authority) {
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
    }

}
