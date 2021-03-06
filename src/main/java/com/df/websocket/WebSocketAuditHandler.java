package com.df.websocket;

import com.df.configuration.MapperComponent;
import com.df.service.AuditService;
import com.df.util.RequestUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class WebSocketAuditHandler implements WebSocketHandler {

    private final AuditService auditService;
    private final MapperComponent mapperComponent;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        Flux<WebSocketMessage> webSocketMessage = webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(id -> {
                    Long parsedId = RequestUtility.convertTextToNumber(id);
                    return parsedId != null ? parsedId : 0L;
                })
                .flatMap(checkedId ->
                        auditService.getById(checkedId)
                                .map(audit -> mapperComponent.writeValueAsString(audit))
                                .map(webSocketSession::textMessage)
                                .switchIfEmpty(
                                        Mono.just("No data found for " + checkedId + " [Audit] id.")
                                                .map(webSocketSession::textMessage))
                )
                .map(m -> m);

        return webSocketSession.send(webSocketMessage)
                .doOnTerminate(() -> log.warn("Terminated [Audit] WebSocket with sessionId = " + webSocketSession.getId()))
                .doOnSubscribe(subscription -> sendMessageOnceOnSubscribe(webSocketSession));
    }

    private void sendMessageOnceOnSubscribe(WebSocketSession webSocketSession) {
        final String subscribeMessage = "Subscribed [Audit] WebSocket with sessionId = " + webSocketSession.getId();
        log.info(subscribeMessage);

        webSocketSession.send(
                Mono.fromCallable(() -> subscribeMessage)
                        .map(webSocketSession::textMessage))
                        .subscribe();
    }

}
