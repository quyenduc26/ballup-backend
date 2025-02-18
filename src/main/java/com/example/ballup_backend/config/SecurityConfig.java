package com.example.ballup_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ballup_backend.util.jwt.JwtAuthenticationFilter;

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
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register", "/auth/google", "/auth/google/callback").permitAll() 
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/auth/oauth2-success", true) )
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/auth/login", "/auth/register").permitAll() // Cho phép truy cập trang chủ & login
    //             .anyRequest().authenticated() // Các request khác yêu cầu đăng nhập
    //         )
    //         .oauth2Login(oauth2 -> oauth2
    //             .loginPage("/auth/signUp")
    //             .defaultSuccessUrl("/home", true) // Redirect sau khi đăng nhập thành công
    //         )
    //         .logout(logout -> logout
    //             .logoutSuccessUrl("/").permitAll()); // Logout về trang chủ

    //     return http.build();
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


