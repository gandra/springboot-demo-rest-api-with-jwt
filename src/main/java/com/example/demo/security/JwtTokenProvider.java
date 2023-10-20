package com.example.demo.security;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-minutes}")
    private long jwtExpirationDateMinutes;

    // generate JWT token
    public String generateToken(Authentication authentication){
        return JwtUtil.generateJwtToken(authentication, jwtExpirationDateMinutes, jwtSecret);
    }

}
