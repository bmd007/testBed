package ir.tiroon.echo.configuration.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
//@EnableWebSocket //use this for simple no broker no stomp web socket
@EnableWebSocketMessageBroker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/exchange")
        config.setApplicationDestinationPrefixes("/app")
//        config.setUserDestinationPrefix("/user")
    }

    //                .setAllowedOrigins("*")
    //is not secure at all. its for test purposes on our remote server
    @Override
    void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/echoStopmEndpoint")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*")

        registry.addEndpoint("/echoStopmEndpoint")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS()
    }

}

