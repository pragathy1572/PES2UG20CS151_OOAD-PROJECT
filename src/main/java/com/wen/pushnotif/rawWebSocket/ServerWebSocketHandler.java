package com.wen.pushnotif.rawWebSocket;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ServerWebSocketHandler.class);
    private final Set<WebSocketSession> sessions  = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        logger.info("server connection opened");
        sessions.add(session);

        TextMessage message = new TextMessage("push notif from server");
        logger.info("sever sends{}");
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        logger.info("session closed");
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 10000)
    void sendPeriodicMessages() throws IOException{
        for(WebSocketSession session:sessions){
            if (session.isOpen()){
                String broadcast = "server periodic message"+ LocalTime.now();
                logger.info("periodic message sent");
                session.sendMessage(new TextMessage(broadcast));

            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception){
        logger.info(exception.getMessage());
    }


    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subporotocol.demo.websocket");
    }
}
