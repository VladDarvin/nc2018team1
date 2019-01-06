package com.nc.airport.backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nc.airport.backend.model.entities.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;
    private final Authority authority;
    private final boolean enabled;
    private final String phoneNumber;

    public JwtUser(
            Long id,
            String firstname,
            String lastname,
            String email,
            String password,
            Authority authority,
            boolean enabled,
            String phoneNumber
    ) {
        this.id = id;
        this.username = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.enabled = enabled;
        this.phoneNumber = phoneNumber;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
