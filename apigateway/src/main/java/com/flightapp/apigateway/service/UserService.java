package com.flightapp.apigateway.service;

import com.flightapp.apigateway.entity.UserDet;
import com.flightapp.apigateway.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDet> userOpt = repo.findByUsername(username);
        if(userOpt.isPresent()){
            UserDet userDet = userOpt.get();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userDet.getRole());
            return new User(userDet.getUsername(), userDet.getPassword(), Arrays.asList(authority));
        }
        else throw new UsernameNotFoundException("Username Not Found");

    }
}
