package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los mensajes que el servidor envía a los clientes (Topic de SOS)
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que el cliente envía al servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // El punto de entrada donde se conectará el Frontend de Alex
        registry.addEndpoint("/ws-habitup")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Soporte para navegadores viejos
    }
}