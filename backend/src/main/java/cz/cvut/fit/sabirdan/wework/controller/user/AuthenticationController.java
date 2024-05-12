package cz.cvut.fit.sabirdan.wework.controller.user;

import cz.cvut.fit.sabirdan.wework.http.request.AuthenticationRequest;
import cz.cvut.fit.sabirdan.wework.http.request.RegisterRequest;
import cz.cvut.fit.sabirdan.wework.http.response.AttributeErrorResponse;
import cz.cvut.fit.sabirdan.wework.http.response.AuthenticationResponse;
import cz.cvut.fit.sabirdan.wework.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Validated RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Validated AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AttributeErrorResponse> authenticationExceptionToInvalidPassword(AuthenticationException ignore) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AttributeErrorResponse(
                        "password",
                        "Invalid password"
                ));
    }
}
