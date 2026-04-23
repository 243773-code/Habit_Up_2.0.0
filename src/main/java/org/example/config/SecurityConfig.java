package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
                // 🔥 NUEVO: Activamos CORS para permitir peticiones desde tu frontend en Vercel
                .cors(Customizer.withDefaults())

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

    // 🔥 NUEVO: Configuración global de CORS detallada
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Usamos OriginPatterns con "*" para permitir cualquier URL (localhost o Vercel) sin romper los WebSockets
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir envío de credenciales (super importante para que los WebSockets no fallen)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicamos esta regla a todas las rutas de la API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}