package com.example.TicketsService.service;

import com.example.TicketsService.exception.JwtValidationException;
import com.example.TicketsService.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private JwtUtils jwtUtils;


    public String getEmailFromHttpServletRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.replace("Bearer ", "").trim();
        try{
            jwtUtils.validateJwtToken(token);
            return jwtUtils.getEmailFromJwtToken(token);
        }
        catch(JwtValidationException e){
            throw e;
        }
    }

    public ObjectId getIdFromHttpServletRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.replace("Bearer ", "").trim();
        try{
            jwtUtils.validateJwtToken(token);
            return new ObjectId(jwtUtils.getIdFromJwtToken(token));
        }
        catch(JwtValidationException e){
            throw e;
        }
    }
}
