package com.flightapp.apigateway.security;

import com.flightapp.apigateway.entity.UserDet;
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

    public String generateToken(String username) {

        String secret = env.getProperty("jwt_secret");
        String validity = env.getProperty("jwt_token_validity");
        Map<String, Object> claims = new HashMap<>();

        Date issueDate = new Date();
        Date expiryDate = new Date(issueDate.getTime() + (Long.valueOf(validity) * 1000));

        String token = Jwts.builder().setClaims(claims)
                .setSubject(username).setIssuedAt(issueDate).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();

        return token;
    }

    public Claims getClaimsFromToken(String token){
        String secret = env.getProperty("jwt_secret");
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims;
    }

    public boolean validateToken(String token, UserDetails user){

        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        Date expiryDate = claims.getExpiration();

        return (user.getUsername().equalsIgnoreCase(username) && expiryDate.after(new Date()));

    }
}
