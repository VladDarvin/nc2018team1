package com.nc.airport.backend.security;

import java.util.List;
import java.util.stream.Collectors;

import com.nc.airport.backend.model.entities.Authority;
import com.nc.airport.backend.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthority(),
                //mapToGrantedAuthorities(user.getAuthorities()),
                user.getEnabled(),
                user.getPhonenumber()
        );
    }

//    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
//        return authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//                .collect(Collectors.toList());
//    }
}
