package com.if_connect.request;

import com.if_connect.models.Mensagem;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketChatClient {

    private static final String URL = "ws://<your-server>/chat";
    private final String encontroId;
    private StompSession session;
    private final Consumer<Mensagem> onMessageReceived;

    public WebSocketChatClient(String encontroId, Consumer<Mensagem> onMessageReceived) {
        this.encontroId = encontroId;
        this.onMessageReceived = onMessageReceived;
    }

    public void connect() throws ExecutionException, InterruptedException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                WebSocketChatClient.this.session = session;
                session.subscribe("/topic/encontros/" + encontroId + "/chat", this);
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Mensagem msg = (Mensagem) payload;
                onMessageReceived.accept(msg);
            }
        };

        session = stompClient.connect(URL, sessionHandler).get();
    }

    public void sendMessage(Mensagem mensagem) {
        if (session != null && session.isConnected()) {
            session.send("/app/sendMessage/" + encontroId, mensagem);
        }
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }
}