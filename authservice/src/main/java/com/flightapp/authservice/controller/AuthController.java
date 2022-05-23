package com.flightapp.authservice.controller;

import com.flightapp.authservice.entity.ErrorResponse;
import com.flightapp.authservice.entity.TokenResponse;
import com.flightapp.authservice.entity.UserDet;
import com.flightapp.authservice.exception.UserAlreadyExistsException;
import com.flightapp.authservice.security.JwtTokenProvider;
import com.flightapp.authservice.service.AuthService;
import com.flightapp.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/service")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private Environment env;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDet request) throws Exception {

        String validity = env.getProperty("jwt.token.validity");
        //Long expiry = (Long.valueOf(validity) * 1000);
        authService.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        Map<String, Object> map = tokenProvider.generateToken(userDetails.getUsername());
        String token = (String) map.get("token");
        Date expiry = (Date) map.get("expiry");

        return new ResponseEntity<>(new TokenResponse(token, expiry, userDetails.getUsername(), role), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDet req) throws UserAlreadyExistsException {
        return new ResponseEntity<>(authService.registerUser(req), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException() {
        return new ResponseEntity<>(new ErrorResponse("User Already Exists"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException() {
        return new ResponseEntity<>(new ErrorResponse("User Not Found"), HttpStatus.NOT_FOUND);
    }


}
