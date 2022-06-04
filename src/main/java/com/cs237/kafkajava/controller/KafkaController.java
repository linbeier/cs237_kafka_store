package com.cs237.kafkajava.controller;


import com.cs237.kafkajava.consumer.MyTopicConsumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.*;

@RestController
public class KafkaController {
    private KafkaTemplate<String, String> template;
    private MyTopicConsumer myTopicConsumer;
    private List<String> CategoryList;

    private Map<String, String> Grocery_map;

    private Random random_number;

    public KafkaController(KafkaTemplate<String, String> template, MyTopicConsumer myTopicConsumer) {
        this.template = template;
        this.myTopicConsumer = myTopicConsumer;
        this.CategoryList = Arrays.asList("Beverages",
                "Bakery",
                "Dairy",
                "Meat",
                "Produce",
                "Personal_Care",
                "Other");
        this.random_number = new Random();
    }

    @GetMapping("/kafka/produce")
    public void produce(@RequestParam HashMap<String, String> message) {
        //control param to topic, Map<String, String>

        template.send(message.get("category"), message.toString());
    }


    Timer timer = new Timer();

    class Task extends TimerTask {
        @Override
        public void run() {
            template.send(Grocery_map.get(""), Grocery_map.toString());
        }

    }

    @GetMapping("/kafka/produce_random")
    public void produce() {
        //control param to topic, Map<String, String>
        int delay = new Random().nextInt(100) * 10;
        timer.schedule(new Task(), delay);
    }

    @GetMapping("/kafka/producetest")
    public void produce(@RequestParam String message) {
        //control param to topic, Map<String, String>

        template.send("meat", message);
    }

    @GetMapping("/kafka/messages")
    public List<String> getMessages() {
        return myTopicConsumer.getMessages();
    }
}
