package com.cs237.kafkajava.consumer;

import com.cs237.kafkajava.controller.MyWebSocket;
import com.cs237.kafkajava.controller.Shoes;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class MyTopicConsumer {

    private final List<String> white_messages = new ArrayList<>();
    private final Queue<String> white_queue = new LinkedList<>();
    private final List<String> black_messages = new ArrayList<>();
    private final Queue<String> black_queue = new LinkedList<>();
    private final int maxlen = 5;
    private final MyWebSocket wscoket;

    public MyTopicConsumer(){
        wscoket = new MyWebSocket();
    }

    @KafkaListener(topics = "White", groupId = "kafka-sandbox")
    public void listen_white(String message) throws IOException {
        System.out.println(message);
        // TODO: push data into database / cache
        synchronized (white_messages) {
            white_messages.add(message);
        }
        // Send this record directly to interesting client session.
        synchronized (wscoket) {
            if (MyWebSocket.topicToWebSocketIdMap.containsKey("White")) {
                for (String sessionId : MyWebSocket.topicToWebSocketIdMap.get("White")) {
                    MyWebSocket.sendMessage(message, MyWebSocket.webSocketMap.get(sessionId));
                }
            }
        }
    }

    @KafkaListener(topics = "Black", groupId = "kafka-sandbox")
    public void listen_black(String message) throws IOException {
        synchronized (black_messages) {
            black_messages.add(message);
        }
        synchronized (wscoket) {
            if (MyWebSocket.topicToWebSocketIdMap.containsKey("Black")) {
                for (String sessionId : MyWebSocket.topicToWebSocketIdMap.get("Black")) {
                    MyWebSocket.sendMessage(message, MyWebSocket.webSocketMap.get(sessionId));
                }
            }
        }
    }

    @KafkaListener(topics = "Multicolor", groupId = "kafka-sandbox")
    public void listen_multicolor(String message) throws IOException {
        synchronized (wscoket) {
            if (MyWebSocket.topicToWebSocketIdMap.containsKey("Multicolor")) {
                for (String sessionId : MyWebSocket.topicToWebSocketIdMap.get("Multicolor")) {
                    MyWebSocket.sendMessage(message, MyWebSocket.webSocketMap.get(sessionId));
                }
            }
        }
    }
    public List<String> getWhite_messages() {
        return white_messages;
    }

    public Queue<String> getWhite_queue(){return white_queue;}

    public List<String> getBlack_messages() {return black_messages;}

    public Queue<String> getBlack_queue(){return black_queue;}

}
