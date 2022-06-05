package com.cs237.kafkajava.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyTopicConsumer {

    private final List<String> white_messages = new ArrayList<>();
    private final List<String> black_messages = new ArrayList<>();

    @KafkaListener(topics = "White", groupId = "kafka-sandbox")
    public void listen_white(String message) {
        synchronized (white_messages) {
            white_messages.add(message);
        }
    }

    @KafkaListener(topics = "Black", groupId = "kafka-sandbox")
    public void listen_black(String message) {
        synchronized (black_messages) {
            black_messages.add(message);
        }
    }

    public List<String> getWhite_messages() {
        return white_messages;
    }

    public List<String> getBlack_messages() {return black_messages;}

}