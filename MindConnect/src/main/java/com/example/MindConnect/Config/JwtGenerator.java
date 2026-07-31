package com.example.MindConnect.Config;

import com.example.MindConnect.Entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



//to create a group, create a post, delete message, send a message, report user, comment,
@Component
public class JwtGenerator {

    @Value("${app.jwt-secret}")
    private String jwtSecret; //generate token using a website

    @Value("${app.jwt-refresh-expiration}")
    private Long refreshExpiration;


    @Value("${app.jwt-expiration}")
    private Long jwtExpiration;


    public String generateAccessToken(Authentication authentication){
        //Authentication anybody can use it, default SpringBoot

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);




        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();

    }

    public String generateAccessToken(UserEntity user){
        //UserEntity custom for this application


        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);




        List<String> authorities = List.of(user.getRole().name());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("authorities", authorities)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();



    }

    public String generateRefreshToken(UserEntity user){

        Date currentDate = new Date();

        Date expiration = new Date(currentDate.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(currentDate)
                .setExpiration(expiration)
                .signWith(key())
                .compact();
    }

    private Key key(){
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(bytes); //encrypts the key
    }


    public String getUsername(String token){

        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException | IllegalArgumentException | SecurityException | MalformedJwtException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> extractAuthorities(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getPayload();
        return claims.get("authorities", List.class);
    }



}
