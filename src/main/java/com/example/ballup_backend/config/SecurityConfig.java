package com.example.ballup_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ballup_backend.util.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register", "/auth/google", "/auth/google/callback").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers("/owner/**").hasAuthority("ROLE_OWNER") 
                .requestMatchers("/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/auth/oauth2-success", true)
                .failureHandler((request, response, exception) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Unauthorized\"}");
            }) )
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized - Please login\"}");
            }));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean(name = "passwordEncoder")
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


