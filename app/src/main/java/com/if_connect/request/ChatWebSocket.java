package com.if_connect.request;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatWebSocket {

    private static ChatWebSocket instance;
    private WebSocket webSocket;
    private final OkHttpClient client;
    private Consumer<String> onMessageReceived;

    private ChatWebSocket() {
        client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(getWebSocketBase())
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Frame de conexão STOMP
                webSocket.send("CONNECT\naccept-version:1.2\nheart-beat:10000,10000\n\n\u0000");
                System.out.println("Conectado ao WebSocket STOMP");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Mensagem recebida: " + text);
                if (onMessageReceived != null) {
                    onMessageReceived.accept(text);
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                System.out.println("WebSocket fechando: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
                System.err.println("Erro no WebSocket: " + t.getMessage());
            }
        });
    }

    public static synchronized ChatWebSocket getInstance() {
        if (instance == null) {
            instance = new ChatWebSocket();
        }
        return instance;
    }

    public void subscribeToTopic(String encontroId) {
        String subscribeFrame = "SUBSCRIBE\n" +
                "id:sub-" + encontroId + "\n" +
                "destination:/topics/live-chat/" + encontroId + "\n\n\u0000";

        webSocket.send(subscribeFrame);
    }

    public void sendMessage(String encontroId, String payloadJson) {
        String sendFrame = "SEND\n" +
                "destination:/app/new-message/" + encontroId + "\n" +
                "content-type:application/json\n\n" +
                payloadJson + "\u0000";

        webSocket.send(sendFrame);
    }

    public void setOnMessageReceived(Consumer<String> callback) {
        this.onMessageReceived = callback;
    }

    private String getWebSocketBase() {
        return Generator.apiUrl.replace("http", "ws")
                .replace("/api/v1/", "") + "/chat/websocket";
    }
}