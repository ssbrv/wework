package cz.cvut.fit.sabirdan.wework.service.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.request.AuthenticationRequest;
import cz.cvut.fit.sabirdan.wework.http.request.ChangePasswordRequest;
import cz.cvut.fit.sabirdan.wework.http.request.LogoutRequest;
import cz.cvut.fit.sabirdan.wework.http.request.RegisterRequest;
import cz.cvut.fit.sabirdan.wework.http.response.user.AuthenticationResponse;
import cz.cvut.fit.sabirdan.wework.service.jwt.JwtService;
import cz.cvut.fit.sabirdan.wework.service.role.system.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SystemRoleService systemRoleService;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getFirstName(),
                registerRequest.getLastName()
        );

        user.setRole(systemRoleService.getUserSystemRole());

        user = userService.save(user);
        String jwtToken = jwtService.generateJwtToken(user);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .id(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userService.getByUsername(authenticationRequest.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        String jwtToken = jwtService.generateJwtToken(user);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .id(user.getId())
                .build();
    }

    public void logout(LogoutRequest logoutRequest) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        jwtService.logout(logoutRequest.getJwtToken(), user);
    }

    public AuthenticationResponse fullLogout() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return fullLogout(user);
    }

    private AuthenticationResponse fullLogout(User user) {
        user.setLastFullLogoutDate(LocalDateTime.now());
        String jwtToken = jwtService.generateJwtToken(user);

        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .id(user.getId())
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        changePasswordRequest.getPassword()
                )
        );

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return fullLogout(user);
    }
}
