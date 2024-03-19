package com.example.TicketsService.security.jwt;

import com.example.TicketsService.exception.JwtValidationException;
import com.example.TicketsService.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtils {

    @Value("${TicketsService.app.jwtSecret}")
    private String jwtSecret;

    @Value("${TicketsService.app.jwtExpirationMs}")
    private int jwtExpirationMs;


    public String generateJwtToken(UserDetailsImpl userDetails){
        return generateTokenFromEmail(userDetails.getEmail());
    }

    public String generateTokenFromEmail(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
    public String getIdFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getId();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        }catch (MalformedJwtException e){
            //logger.error("Inwalid JWT token: {}", e.getMessage());
            throw new JwtValidationException("Invalid JWT token");
        }catch (ExpiredJwtException e){
            //logger.error("JWT token is expired: {}", e.getMessage());
            throw new JwtValidationException("JWT token is expired");
        }catch (UnsupportedJwtException e){
            //logger.error("JWT token is unsupported: {}", e.getMessage());
            throw new JwtValidationException("JWT token is unsupported");
        }catch (IllegalArgumentException e){
            //logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtValidationException("JWT claims string is empty");
        }
  //      return false;
    }

}
