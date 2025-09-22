package com.fiap.studyrooms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/rooms", "/css/**", "/js/**", "/webjars/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(Customizer.withDefaults())
      .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
      .csrf(csrf -> csrf.ignoringRequestMatchers("/rooms/**")); // simplificação p/ demo de POST

    return http.build();
  }
}