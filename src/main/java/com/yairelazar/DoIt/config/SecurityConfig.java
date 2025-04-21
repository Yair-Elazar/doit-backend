package com.yairelazar.DoIt.config;

import com.yairelazar.DoIt.security.JwtAuthFilter;  // הוסף את המחלקה JwtAuthFilter
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;  // הוסף שדה למחלקה

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {  // הוסף את ה־JwtAuthFilter ל constructor
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()  // מתיר גישה לרישום והתחברות
                        .anyRequest().authenticated()  // כל הבקשות האחרות ידרשו אימות
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  // הוסף את ה־JwtAuthFilter כאן
                .csrf(csrf -> csrf.disable());  // משבית את CSRF

        return http.build();
    }
}
