package com.nc.airport.backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nc.airport.backend.model.entities.model.users.Authority;
import com.nc.airport.backend.model.entities.model.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {

    private String login;
    private String password;
    private Boolean enabled;
    private List<Authority> authorities = new ArrayList<>();


    public JwtUser(User user) {
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.enabled = user.getEnabled();
        this.authorities.add(user.getAuthority());
    }

    @Override
    public String getUsername() {
        return login;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
