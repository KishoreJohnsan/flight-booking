package com.flightapp.authservice.controller;

import com.flightapp.authservice.entity.ErrorResponse;
import com.flightapp.authservice.entity.TokenResponse;
import com.flightapp.authservice.entity.UserDet;
import com.flightapp.authservice.exception.UserAlreadyExistsException;
import com.flightapp.authservice.security.JwtTokenProvider;
import com.flightapp.authservice.service.AuthService;
import com.flightapp.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/service")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDet request) throws Exception {

        authService.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = tokenProvider.generateToken(userDetails.getUsername());
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDet req) throws UserAlreadyExistsException {
        return new ResponseEntity<>(authService.registerUser(req), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException() {
        return new ResponseEntity<>(new ErrorResponse("User Already Exists"), HttpStatus.CONFLICT);
    }




}
