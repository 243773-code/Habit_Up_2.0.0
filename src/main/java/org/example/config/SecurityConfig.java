package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF para pruebas en Insomnia y WebSockets
                .csrf(AbstractHttpConfigurer::disable)

                // 2. IMPORTANTE: Ajuste para que SockJS funcione
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 3. Definición de permisos de acceso
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos para desarrollo
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/habits/**").permitAll()
                        .requestMatchers("/api/achievements/**").permitAll()
                        .requestMatchers("/api/dashboard/**").permitAll()
                        .requestMatchers("/api/posts/**").permitAll()
                        .requestMatchers("/api/relapses/**").permitAll()

                        // 🔥 SOS: Botón de pánico
                        .requestMatchers("/api/sos/**").permitAll()

                        // 🔥 COMMENTS: Permitimos la interacción social
                        .requestMatchers("/api/comments/**").permitAll()

                        // 🔥 Endpoint de WebSockets
                        .requestMatchers("/ws-habitup/**").permitAll()

                        // Todo lo demás bloqueado
                        .anyRequest().authenticated()
                )

                // 4. Desactivamos login por formulario y Basic Auth
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}