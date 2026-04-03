package com.zorvyn.finance.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                // Disable CSRF as we use JWT tokens (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Use stateless session - no server side sessions
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Custom error responses for auth failures
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(401);
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(403);
                            response.getWriter().write("{\"error\": \"Access denied - insufficient permissions\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no token required
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        // Records - all roles can view
                        .requestMatchers(HttpMethod.GET, "/api/records/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        // Records - only ANALYST and ADMIN can create/update
                        .requestMatchers(HttpMethod.POST, "/api/records/**")
                        .hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/records/**")
                        .hasAnyRole("ANALYST", "ADMIN")

                        // Records - only ADMIN can delete
                        .requestMatchers(HttpMethod.DELETE, "/api/records/**")
                        .hasRole("ADMIN")

                        // Users - only ADMIN can manage
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // Dashboard - all roles can view
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before Spring's auth filter
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt password encoder for secure password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}