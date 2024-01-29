package com.example.TicketsService.security.service;

import com.example.TicketsService.exception.TokenRefreshException;
import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.repository.RefreshTokenRepository;
import com.example.TicketsService.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

        @Value("${TicketsService.app.jwtRefreshExpiratiobMS}")
        private Long refreshTokenExpirationMs;

        @Autowired
        private RefreshTokenRepository refreshTokenRepository;

        @Autowired
        private UserRepository userRepository;

        public Optional<RefreshTokenEntity> findByToken(String token){
            return refreshTokenRepository.findByToken(token);
        }

        public RefreshTokenEntity createRefreshToken(ObjectId userId){
            RefreshTokenEntity refreshToken = new RefreshTokenEntity();

            refreshToken.setUserEntity(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }

        public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token){
            if(token.getExpiryDate().compareTo(Instant.now()) < 0){
                refreshTokenRepository.delete(token);
                throw new TokenRefreshException(token.getToken(), "Refresh token was expired");
            }

            return token;
        }

        public int deleteByUserId(ObjectId userId){
            return refreshTokenRepository.deleteByUserEntity(userRepository.findById(userId).get());
        }
}
