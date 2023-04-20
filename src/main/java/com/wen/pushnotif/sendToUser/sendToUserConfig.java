package com.wen.pushnotif.sendToUser;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class sendToUserConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic/", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/greeting").setHandshakeHandler(new DefaultHandshakeHandler() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map atttributes) {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest serveletRequest = (ServletServerHttpRequest) request;
                    HttpSession session = serveletRequest.getServletRequest().getSession();
                    atttributes.put("sessionid", session.getId());
                }
                return true;
            }
        }).withSockJS();
//        using sockJs as a fallback to the websockets
    }
}
