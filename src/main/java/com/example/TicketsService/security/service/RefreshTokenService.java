package com.example.TicketsService.security.service;

import com.example.TicketsService.dto.request.TokenRefreshRequest;
import com.example.TicketsService.dto.response.TokenRefreshResponse;
import com.example.TicketsService.exception.TokenRefreshException;
import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.RefreshTokenRepository;
import com.example.TicketsService.repository.UserRepository;
import com.example.TicketsService.security.jwt.JwtUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

        @Value("${TicketsService.app.jwtRefreshExpiratiobMS}")
        private Long refreshTokenExpirationMs;

        private final RefreshTokenRepository refreshTokenRepository;

        private final UserRepository userRepository;

        private final JwtUtils jwtUtils;

        @Autowired
        public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtUtils jwtUtils) {
            this.refreshTokenRepository = refreshTokenRepository;
            this.userRepository = userRepository;
            this.jwtUtils = jwtUtils;
        }


        @Transactional
        public ResponseEntity<?> refreshToken(TokenRefreshRequest refreshRequest){
            String requestRefreshToken = refreshRequest.getRefreshToken();

            RefreshTokenEntity refreshToken = findByToken(requestRefreshToken)
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database"));

            verifyExpiration(refreshToken);
            UserEntity user = refreshToken.getUserEntity();

            String token = jwtUtils.generateTokenFromEmail(user.getEmail());

            deleteByUserId(user.getIdAsObjectId());
            RefreshTokenEntity newRefreshToken = createRefreshToken(user.getIdAsObjectId());

            return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken.getToken()));
        }

        public Optional<RefreshTokenEntity> findByToken(String token){
            return refreshTokenRepository.findByToken(token);
        }

        @Transactional
        public RefreshTokenEntity createRefreshToken(ObjectId userId){
            RefreshTokenEntity refreshToken = new RefreshTokenEntity();

            refreshToken.setUserEntity(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            //token po wygaśnięciu zostaje usunięty z bazy danych poprzez trigger w mongoDB
            //TODO oraz raz na godzinę sprawdzamy czy to się wykonuje poprawnie
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

        @Transactional
        public int deleteByUserId(ObjectId userId){
            return refreshTokenRepository.deleteByUserEntity(userRepository.findById(userId).get());
        }
}
