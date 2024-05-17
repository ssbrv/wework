package cz.cvut.fit.sabirdan.wework.config.security;

import cz.cvut.fit.sabirdan.wework.config.web.JwtExceptionFilter;
import cz.cvut.fit.sabirdan.wework.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/api/v1/user/**").authenticated()
                                .requestMatchers("/api/v1/membership/**").authenticated()
                                .requestMatchers("/api/v1/task/**").authenticated()
                                .requestMatchers("/api/v1/project/**").authenticated()
                                .requestMatchers("/api/v1/system-role/**").authenticated()
                                .requestMatchers("/api/v1/member-role/**").authenticated()
                                .requestMatchers("/api/v1/auth/logout").authenticated()
                                .requestMatchers("/api/v1/auth/full-logout").authenticated()
                                .requestMatchers("/api/v1/auth/password").authenticated()
                                .anyRequest().permitAll()
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
