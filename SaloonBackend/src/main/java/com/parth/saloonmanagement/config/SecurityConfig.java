package com.parth.saloonmanagement.config;

import org.springframework.security.config.Customizer;
import com.parth.saloonmanagement.security.JwtAuthenticationFilter;
import com.parth.saloonmanagement.security.JwtService;
import com.parth.saloonmanagement.security.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserDetailService userDetailService

    ) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtService ,userDetailService);

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();

                    config.setAllowedOrigins(
                            java.util.List.of("http://localhost:5173")
                    );
                    config.setAllowedMethods(
                            java.util.List.of("*")
                    );
                    config.setAllowedHeaders(
                            java.util.List.of("*")
                    );
                    config.setAllowCredentials(true);

                    return config;
                }))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/tenants/**").permitAll()
                        .requestMatchers("/api/stylists/me/bookings").hasRole("STYLIST")
                        .requestMatchers("/api/stylists/**").hasRole("OWNER")
                        .requestMatchers("/api/treatments/**").hasRole("OWNER")
                        .requestMatchers("/api/bookings/**").hasAnyRole("OWNER", "CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
