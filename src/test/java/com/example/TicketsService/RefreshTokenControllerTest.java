package com.example.TicketsService;

import com.example.TicketsService.dto.request.TokenRefreshRequest;
import com.example.TicketsService.dto.response.TokenRefreshResponse;
import com.example.TicketsService.exception.TokenRefreshException;
import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.RefreshTokenRepository;
import com.example.TicketsService.repository.UserRepository;
import com.example.TicketsService.security.jwt.JwtUtils;
import com.example.TicketsService.security.service.RefreshTokenService;
import com.example.TicketsService.security.service.TokenProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RefreshTokenControllerTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @BeforeEach
    void setUp() {
        refreshTokenService.setRefreshTokenExpirationMs(1000L);
    }
    @Test
    void refreshToken_ValidRefreshToken_ReturnsNewTokens() {
        // given
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("Valid-Refresh-Token");
        ObjectId userId = new ObjectId();
        UserEntity user = createNewUser(userId);
        RefreshTokenEntity validRefreshToken = createNewValidRefreshToken(user);


        Mockito.when(refreshTokenRepository.findByToken("Valid-Refresh-Token"))
                .thenReturn(Optional.of(validRefreshToken));

        Mockito.when(jwtUtils.generateTokenFromEmail(user.getEmail()))
                .thenReturn("New-Access-Token");

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(refreshTokenRepository.save(Mockito.any(RefreshTokenEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(tokenProvider.generateToken()).thenReturn("New-Refresh-Token");
        // when
        ResponseEntity<?> response = refreshTokenService.refreshToken(refreshRequest);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof TokenRefreshResponse);

        TokenRefreshResponse refreshResponse = (TokenRefreshResponse) response.getBody();
        Assertions.assertEquals("New-Access-Token", refreshResponse.getAccessToken());
        Assertions.assertEquals("New-Refresh-Token", refreshResponse.getRefreshToken());

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).findByToken("Valid-Refresh-Token");
        Mockito.verify(jwtUtils, Mockito.times(1)).generateTokenFromEmail(user.getEmail());
        Mockito.verify(userRepository, Mockito.times(2)).findById(userId);
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).save(Mockito.any(RefreshTokenEntity.class));
        Mockito.verify(tokenProvider, Mockito.times(1)).generateToken();
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).deleteByUserEntity(user);
    }

    private UserEntity createNewUser(ObjectId userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("test@example.com");
        return user;
    }

    private RefreshTokenEntity createNewValidRefreshToken(UserEntity user) {
        RefreshTokenEntity validRefreshToken = new RefreshTokenEntity();
        validRefreshToken.setToken("Valid-Refresh-Token");
        validRefreshToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));
        validRefreshToken.setUserEntity(user);
        return validRefreshToken;
    }

    @Test
    void refreshToken_InvalidRefreshToken_ReturnsBadRequest() {
        // given
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("Invalid-Refresh-Token");

        Mockito.when(refreshTokenService.findByToken("Invalid-Refresh-Token"))
                .thenReturn(Optional.empty());

        // when
        TokenRefreshException exception = assertThrows(TokenRefreshException.class, () -> {
            refreshTokenService.refreshToken(refreshRequest);
        });
        // then
        Assertions.assertEquals("Failed for [Invalid-Refresh-Token]: Refresh token is not in database", exception.getMessage());
    }
    @Test
    void refreshToken_ExpiredRefreshToken_ThrowtesTokenRefreshException() {
        // given
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("Expired-Refresh-Token");

        RefreshTokenEntity expiredRefreshToken = new RefreshTokenEntity();
        expiredRefreshToken.setToken("Expired-Refresh-Token");
        expiredRefreshToken.setExpiryDate(Instant.now().minus(1, ChronoUnit.DAYS));

        Mockito.when(refreshTokenService.findByToken("Expired-Refresh-Token"))
                .thenReturn(Optional.of(expiredRefreshToken));

        // when
        TokenRefreshException exception = assertThrows(TokenRefreshException.class, () -> {
            refreshTokenService.refreshToken(refreshRequest);
        });

        // then
        Assertions.assertEquals("Failed for [Expired-Refresh-Token]: Refresh token was expired", exception.getMessage());
    }

}
