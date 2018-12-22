package com.nc.airport.backend.security;

import com.nc.airport.backend.model.entities.User;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getObjectId(),
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
