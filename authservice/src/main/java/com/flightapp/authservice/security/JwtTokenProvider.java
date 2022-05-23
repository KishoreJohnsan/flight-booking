package com.flightapp.authservice.security;

import com.flightapp.authservice.entity.UserDet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Autowired
    Environment env;

    public Map<String, Object> generateToken(String username) {

        Map<String, Object> map = new HashMap<>();

        String secret = env.getProperty("jwt.secret");
        String validity = env.getProperty("jwt.token.validity");
        Map<String, Object> claims = new HashMap<>();

        Date issueDate = new Date();
        Date expiryDate = new Date(issueDate.getTime() + (Long.valueOf(validity) * 1000));

        String token =  Jwts.builder().setClaims(claims)
                .setSubject(username).setIssuedAt(issueDate).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();

        map.put("token", token);
        map.put("expiry", expiryDate);
        return map;
    }

    public Claims getClaimsFromToken(String token){
        String secret = env.getProperty("jwt.secret");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails user){

        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        Date expiryDate = claims.getExpiration();

        return (user.getUsername().equalsIgnoreCase(username) && expiryDate.after(new Date()));

    }
}
