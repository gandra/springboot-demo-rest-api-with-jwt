package com.example.demo.util;


import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.exception.MyDemoAppException;
import com.example.demo.security.SecurityUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtUtil {

    public static Key generateJwtKey(String jwtSecret){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    }

    public static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public static String populateAuthorities(Set<Authority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for (Authority authority : authorities) {
            authoritiesSet.add(authority.getName());
        }
        return String.join(",", authoritiesSet);
    }

    public static String generateJwtToken(Authentication authentication, long jwtExpirationDateMinutes, String jwtSecret) {
        return generateJwtToken(((SecurityUser)authentication.getPrincipal()).getUser(), jwtExpirationDateMinutes, jwtSecret);
    }
    // generate JWT token
    public static String generateJwtToken(User user, long jwtExpirationDateMinutes, String jwtSecret) {
        String username = user.getEmail();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDateMinutes * 60 * 1000);

        String token = Jwts.builder()
            .setIssuer("My Demo App")
            .claim("authorities", populateAuthorities(user.getAuthorities()))
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(JwtUtil.generateJwtKey(jwtSecret))
            .compact();
        return token;
    }

    // get username from Jwt token
    public String getUsername(String token, String jwtSecret){
        Claims claims = Jwts.parser()
            .setSigningKey(generateJwtKey(jwtSecret))
            .build()
            .parseClaimsJws(token)
            .getBody();
        String username = claims.getSubject();
        return username;
    }

    // validate Jwt token
    public boolean validateToken(String token, String jwtSecret){
        try{
            Jwts.parser()
                .setSigningKey(generateJwtKey(jwtSecret))
                .build()
                .parse(token);
            return true;
        } catch (MalformedJwtException ex) {
            throw new MyDemoAppException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new MyDemoAppException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new MyDemoAppException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new MyDemoAppException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}
