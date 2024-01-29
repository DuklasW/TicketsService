package com.example.TicketsService.service;

import com.example.TicketsService.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private JwtUtils jwtUtils;


    public String getEmailFromHttpServletRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.replace("Bearer ", "").trim();
        return jwtUtils.getEmailFromJwtToken(token);
    }
}
