package org.exalt.task2.config;

import lombok.RequiredArgsConstructor;
import org.exalt.task2.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Doctor endpoints
                        .requestMatchers(HttpMethod.POST, "/api/doctors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                        // Patient endpoints
                        .requestMatchers(HttpMethod.POST, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")

                        // Appointment endpoints
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasRole("PATIENT")
                        .requestMatchers("/api/appointments/*/cancel").hasRole("PATIENT")
                        .requestMatchers("/api/appointments/*/complete").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                        // Prescription endpoints
                        .requestMatchers(HttpMethod.POST, "/api/prescriptions").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/api/prescriptions/**").hasAnyRole("PATIENT", "DOCTOR")

                        // Medical records
                        .requestMatchers("/api/medical-records/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                        // Actuator
                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});  // HTTP Basic Auth

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // no args
        provider.setUserDetailsService(userDetailsService); // set the service
        provider.setPasswordEncoder(passwordEncoder());     // set the encoder
        return provider;
    }


    @Bean("hospitalPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}