package com.flightapp.authservice.service;

import com.flightapp.authservice.entity.UserDet;
import com.flightapp.authservice.exception.UserAlreadyExistsException;
import com.flightapp.authservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void authenticate(String username, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public boolean registerUser(UserDet userDet) throws UserAlreadyExistsException {
        Optional<UserDet> userOpt = repo.findByUsername(userDet.getUsername());
        if(userOpt.isPresent())
            throw new UserAlreadyExistsException();
        else {
            userDet.setRole("user");
            userDet.setPassword(passwordEncoder.encode(userDet.getPassword()));
            repo.save(userDet);
        }
        return true;
    }
}
