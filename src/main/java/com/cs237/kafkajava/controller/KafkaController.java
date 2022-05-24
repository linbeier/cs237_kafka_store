package com.cs237.kafkajava.controller;


import com.cs237.kafkajava.consumer.MyTopicConsumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class KafkaController {
    private KafkaTemplate<String, String> template;
    private MyTopicConsumer myTopicConsumer;
    private List<String> CategoryList;

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
    }

    @GetMapping("/kafka/produce")
    public void produce(@RequestParam HashMap<String, String> message) {
        //control param to topic, Map<String, String>

        template.send(message.get("category"), message.toString());
    }

    @GetMapping("/kafka/produceonce")
    public void produce(@RequestParam String message) {
        //control param to topic, Map<String, String>

        template.send("meat", message);
    }

    @GetMapping("/kafka/messages")
    public List<String> getMessages() {
        return myTopicConsumer.getMessages();
    }
}
