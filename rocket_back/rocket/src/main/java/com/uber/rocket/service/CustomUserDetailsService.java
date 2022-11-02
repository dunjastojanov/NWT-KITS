package com.uber.rocket.service;

import com.uber.rocket.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        client.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority(role.getRole()));
//        });
//        User user=new User(client.getEmail(), client.getPassword(), authorities);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);//, client.isBlocked(), true, true, true, authorities);
//        return new User(client.getEmail(), client.getPassword());//, client.isBlocked(), true, true, true, authorities);
    }
}
