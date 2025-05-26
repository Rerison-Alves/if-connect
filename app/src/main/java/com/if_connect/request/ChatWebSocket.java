package com.if_connect.request;

import androidx.annotation.NonNull;

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
    private Runnable onConnected;
    private Consumer<String> onMessageReceived;

    private ChatWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(getWebSocketBase())
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                webSocket.send("CONNECT\naccept-version:1.2\nheart-beat:10000,10000\n\n\u0000");
                if (onConnected != null) {
                    onConnected.run();
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                if (onMessageReceived != null) {
                    onMessageReceived.accept(text);
                }
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {

            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {

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

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Conexão encerrada pelo cliente");
            webSocket = null;
            instance = null;
        }
    }

    public void setOnConnected(Runnable onConnected) {
        this.onConnected = onConnected;
    }

    public void setOnMessageReceived(Consumer<String> callback) {
        this.onMessageReceived = callback;
    }

    private String getWebSocketBase() {
        return Generator.apiUrl.replace("http", "ws")
                .replace("/api/v1/", "") + "/chat/websocket";
    }
}