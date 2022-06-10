package com.cs237.kafkajava.consumer;

import com.cs237.kafkajava.controller.MyWebSocket;
import com.cs237.kafkajava.controller.ProductService;
import com.cs237.kafkajava.controller.Shoes;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MyWebSocket wscoket;

    public MyTopicConsumer(){
        wscoket = new MyWebSocket();
    }

    @Autowired
    ProductService productService;

    @KafkaListener(topics = "White", groupId = "kafka-sandbox")
    public void listen_white(String message) throws IOException {
        synchronized (white_messages) {
            white_messages.add(message);
        }
        if(wscoket.session_exsit){
            wscoket.sendMessage(message);
        }
        Shoes shoe = new Gson().fromJson(message, Shoes.class);
        System.out.println(shoe.getImage_url());
        productService.insertProduct(shoe);
    }

    @KafkaListener(topics = "Black", groupId = "kafka-sandbox")
    public void listen_black(String message) {
        synchronized (black_messages) {
            black_messages.add(message);
        }
        synchronized (black_queue) {
            if(black_queue.size() < maxlen){
                black_queue.add(message);
            }else{
                black_queue.poll();
                black_queue.add(message);
            }
        }
        Shoes shoe = new Gson().fromJson(message, Shoes.class);
        System.out.println(shoe.getImage_url());
//        System.out.println(sizeOf(shoe.getImage_url()));
        productService.insertProduct(shoe);
    }

    public List<String> getWhite_messages() {
        return white_messages;
    }

    public Queue<String> getWhite_queue(){return white_queue;}

    public List<String> getBlack_messages() {return black_messages;}

    public Queue<String> getBlack_queue(){return black_queue;}

}