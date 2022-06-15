package com.df.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebFluxConfig {

    private WebFluxSocketAuditHandler handleAudit;

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> handlerMap = Map.ofEntries(
                Map.entry("/ws/audit", handleAudit)
        );
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }

    @Bean
    public WebSocketService webSocketService() {
        return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    }

}
