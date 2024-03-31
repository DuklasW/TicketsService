package com.example.TicketsService.validate;

import com.example.TicketsService.dto.request.SignUpConsumerRequest;
import com.example.TicketsService.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(Object object) throws ValidationException {

        if (!(object instanceof SignUpConsumerRequest signUpRequest)) {
            throw new IllegalArgumentException("Invalid object type");
        }

        if (userService.checkUserExistByEmail(signUpRequest.getEmail())) {
            throw new ValidationException("Error: Email is already taken");
        }
        if (!signUpRequest.isValidRegon()) {
            throw new ValidationException("Error: regon not in enum list");
        }
        throw new ValidationException("Error: Wszystko gicik!");
    }

}
