//package com.example.TicketsService.service;
//
//import com.example.TicketsService.exception.JwtValidationException;
//import com.example.TicketsService.security.jwt.JwtUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
/*
Currently unused functions that may be useful in the future were used to capture email or user id from HttpServletRequest
 */

//@Service
//public class TokenService {
//
//    private final JwtUtils jwtUtils;
//
//    @Autowired
//    public TokenService(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//
//
//    public String getEmailFromHttpServletRequest(HttpServletRequest request){
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = authorizationHeader.replace("Bearer ", "").trim();
//        try{
//            jwtUtils.validateJwtToken(token);
//            return jwtUtils.getEmailFromJwtToken(token);
//        }
//        catch(JwtValidationException e){
//            throw e;
//        }
//    }
//
//    public ObjectId getIdFromHttpServletRequest(HttpServletRequest request){
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = authorizationHeader.replace("Bearer ", "").trim();
//        try{
//            jwtUtils.validateJwtToken(token);
//            return new ObjectId(jwtUtils.getIdFromJwtToken(token));
//        }
//        catch(JwtValidationException e){
//            throw e;
//        }
//    }
//}
