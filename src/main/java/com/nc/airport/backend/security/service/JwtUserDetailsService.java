package com.nc.airport.backend.security.service;

import com.nc.airport.backend.model.entities.model.users.User;
import com.nc.airport.backend.security.JwtUser;
import com.nc.airport.backend.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserService service;

    public JwtUserDetailsService(UserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = service.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with login " + login);
        } else {
            return new JwtUser(user);
        }
    }
}
