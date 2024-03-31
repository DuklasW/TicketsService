package com.example.TicketsService;

import com.example.TicketsService.Factory.ProfileFactory;
import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.dto.request.SignUpConsumerRequest;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.enums.RoleEnum;
import com.example.TicketsService.service.AuthService;
import com.example.TicketsService.service.ConsumerService;
import com.example.TicketsService.service.UserService;
import com.example.TicketsService.validate.UserValidator;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserService userService;

    @Mock
    private ConsumerService consumerService;

    @Mock
    private ProfileFactory profileFactory;

    @Mock
    private PasswordEncoder passwordEncoder; //PasswordEncoder has to be mocked, but it doesn't have to be used, but it's better if it is
    @Test
    void registerConsumer_shouldReturnOkResponse_whenValidRequest() {
        // given
        SignUpConsumerRequest request = createSignUpConsumerRequest();
        Set<RoleEnum> userRoles = Collections.singleton(RoleEnum.ROLE_CONSUMER);
        ObjectId userId = new ObjectId();
        ConsumerEntity savedConsumer = new ConsumerEntity();
        savedConsumer.setId(new ObjectId());

        Mockito.doNothing().when(userValidator).validate(request);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userService.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            Assertions.assertEquals(request.getEmail(), user.getEmail());
            Assertions.assertEquals("encodedPassword", user.getPassword());
            Assertions.assertEquals(userRoles, user.getRoles());
            user.setId(userId);
            return user;
        });

        Mockito.when(profileFactory.createConsumer(Mockito.eq(userId.toString()), Mockito.eq(request))).thenAnswer(invocation -> {
            String userIdArg = invocation.getArgument(0);
            SignUpConsumerRequest requestArg = invocation.getArgument(1);
            Assertions.assertEquals(userId.toString(), userIdArg);
            Assertions.assertEquals(request, requestArg);
            return savedConsumer;
        });
        Mockito.when(consumerService.save(Mockito.any(ConsumerEntity.class))).thenAnswer(invocation -> {
            ConsumerEntity consumer = invocation.getArgument(0);
            Assertions.assertEquals(savedConsumer, consumer);
            return consumer;
        });

        // when
        ResponseEntity<MessageResponse> response = authService.registerConsumer(request);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Consumer registered successfully!", response.getBody().getMessage());

        Mockito.verify(userValidator, Mockito.times(1)).validate(request);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(request.getPassword());
        Mockito.verify(userService, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Mockito.verify(profileFactory, Mockito.times(1)).createConsumer(Mockito.eq(userId.toHexString()), Mockito.eq(request));
        Mockito.verify(consumerService, Mockito.times(1)).save(Mockito.any(ConsumerEntity.class));
        Mockito.verify(userService, Mockito.never()).deleteByUserId(Mockito.any(ObjectId.class));
    }


    @Test
    void registerConsumer_shouldReturnBadRequestResponse_whenInvalidRequest() {
        // given
        SignUpConsumerRequest invalidRequest = createBadSignUpConsumerRequest();

        Mockito.doThrow(new ValidationException("Error: Email is already taken")).when(userValidator).validate(invalidRequest);

        // when
        ResponseEntity<MessageResponse> response = authService.registerConsumer(invalidRequest);
        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Error: Email is already taken", response.getBody().getMessage());

        Mockito.verify(userValidator, Mockito.times(1)).validate(invalidRequest);
        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.anyString());
        Mockito.verify(userService, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(profileFactory, Mockito.never()).createConsumer(Mockito.anyString(), Mockito.any(SignUpConsumerRequest.class));
        Mockito.verify(consumerService, Mockito.never()).save(Mockito.any(ConsumerEntity.class));
        Mockito.verify(userService, Mockito.never()).deleteByUserId(Mockito.any(ObjectId.class));
    }

    private SignUpConsumerRequest createSignUpConsumerRequest() {
        SignUpConsumerRequest request = new SignUpConsumerRequest();
        request.setEmail("jasiek3@user.com");
        request.setPassword("Passwordini123@");
        request.setName("Jan");
        request.setSurname("Testowy");
        request.setPhone("123456789");
        request.setRegon("Kujawsko-Pomorskie");
        request.setCity("Toruń");
        request.setPostcode("87-100");
        request.setStreet("Pieskowa 18");
        return request;
    }

    private SignUpConsumerRequest createBadSignUpConsumerRequest() {
        SignUpConsumerRequest request = new SignUpConsumerRequest();
        request.setEmail("jasiek3");
        request.setPassword("short");
        request.setName("Jan");
        request.setSurname("Testowy");
        request.setPhone("123456789");
        request.setRegon("kujawisko");
        request.setCity("Toruń");
        request.setPostcode("87-100");
        request.setStreet("Pieskowa 18");
        return request;
    }
}
