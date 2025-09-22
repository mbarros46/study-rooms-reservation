package br.com.fiap.epictaskg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/login**",
                        "/oauth2/**",
                        "/error",
                        "/css/**", "/js/**", "/images/**", "/webjars/**"
                ).permitAll()
                .requestMatchers("/rooms").authenticated()  // Permitir visualizar salas para todos autenticados
                .requestMatchers("/rooms/new", "/rooms/*/edit", "/rooms/create", "/rooms/*/update", "/rooms/*/delete").hasRole("LIBRARIAN")  // Apenas LIBRARIAN pode criar/editar
                .requestMatchers("/reservations/approve/**", "/reservations/cancel/**", "/reservations/admin").hasRole("LIBRARIAN")  // Apenas LIBRARIAN pode aprovar/cancelar
                .requestMatchers("/reservations/**").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(ui -> ui.userService(customOAuth2UserService))
                .defaultSuccessUrl("/reservations/mine", true)
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

        return http.build();
    }
}
