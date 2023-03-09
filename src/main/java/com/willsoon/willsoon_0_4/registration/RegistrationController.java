package com.willsoon.willsoon_0_4.registration;

import com.willsoon.willsoon_0_4.security.config.ErrorResponse;
import com.willsoon.willsoon_0_4.security.config.customExceptions.EmailAlreadyExistsException;
import com.willsoon.willsoon_0_4.security.config.customExceptions.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            registrationService.register(request);
            return ResponseEntity.ok().build();
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Username already exists"));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Email already exists"));
        } catch (MailSendException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to send email"));
        }
    }

    @PostMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
