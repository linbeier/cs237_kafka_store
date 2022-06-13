package com.cs237.kafkajava.controller;


import com.cs237.kafkajava.consumer.MyTopicConsumer;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;

@RestController
public class KafkaController {
    private KafkaTemplate<String, String> template;
    private MyTopicConsumer myTopicConsumer;

    private List<Shoes> Grocery_map;

    private static Random random_number;
    private String csv_path;
    private int Shoes_index;

    public static ConcurrentHashMap<Integer, Map<String, Long>> timeStore;
    private int shoes_produced;

    public KafkaController(KafkaTemplate<String, String> template, MyTopicConsumer myTopicConsumer) throws FileNotFoundException {
        this.template = template;
        this.myTopicConsumer = myTopicConsumer;
        this.csv_path = "src/main/resources/Mock-data-mens-shoes.csv";
        random_number = new Random(System.currentTimeMillis());
        this.Grocery_map = new CsvToBeanBuilder(new FileReader(this.csv_path))
                .withType(Shoes.class)
                .build()
                .parse();
        int record_count = 0;
        for (Shoes s:this.Grocery_map) {
            s.set("id", String.valueOf(record_count));
            record_count++;
        }
        this.Shoes_index = 0;
        this.shoes_produced = 0;
        if(timeStore == null)timeStore = new ConcurrentHashMap<>();
        String[] colors_fake = {"White", "Black", "Multicolor"};
        for (Shoes p : this.Grocery_map) {
            p.set("colors", colors_fake[random_number.nextInt(3)]);
        };

    }

    static public double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = random_number.nextGaussian();
        double biasFactor = Math.exp(bias);
        double retval = mid + (range * (biasFactor / (biasFactor + Math.exp(-unitGaussian/skew)) - 0.5));
        return retval;
    }
    Timer timer = new Timer();

    class Task extends TimerTask {
        @Override
        public void run() {
            if(Shoes_index >= Grocery_map.size()){
                Shoes_index = 0;
            };
            int shoes_quantity = (int) Grocery_map.get(Shoes_index).get("quantity");
            if(shoes_quantity <= 1){
                shoes_quantity = random_number.nextInt(1000);
            }
            shoes_quantity -= random_number.nextInt(shoes_quantity);
            if(shoes_quantity <= 1){
                shoes_quantity = random_number.nextInt(1000);
            }
            if(shoes_quantity >= 1000){
                shoes_quantity = shoes_quantity % 1000;
            }
            Grocery_map.get(Shoes_index).set("quantity", String.valueOf(shoes_quantity));
            Grocery_map.get(Shoes_index).set("producetime", String.valueOf(System.currentTimeMillis()));
            template.send((String) Grocery_map.get(Shoes_index).get("colors"), new Gson().toJson(Grocery_map.get(Shoes_index)));
//            Map<String, Long> m = new HashMap<>();
//            m.put("produce_start", System.currentTimeMillis());
//            timeStore.put((Integer) Grocery_map.get(Shoes_index).get("id"), m);

            Shoes_index += 1;
            shoes_produced++;
            if(shoes_produced % 1000 == 0){
                System.out.println("shoes have been produced: " +  shoes_produced + "\n");
            }
        }

    }

    @GetMapping("/kafka/produce_random")
    public void produce() throws InterruptedException {
        //control param to topic, Map<String, String>
        int size = Grocery_map.size();
        int count = 0;
        while(count < size){
            int delay = new Random().nextInt(100) * 100;
            timer.schedule(new Task(), delay);
            TimeUnit.MILLISECONDS.sleep(100);
            count++;
        }

    }

    @GetMapping("/kafka/produce_skew")
    public void produce_skew(){
        int size = Grocery_map.size();
        int count = 0;
        while(count < 2 * size){
            long delay = (long)nextSkewedBoundedDouble(0, 5000, 1, 1);
            timer.schedule(new Task(), delay);
            count++;
        }
    }

    @GetMapping("/kafka/history_product")
    public String history_product(@RequestParam String color) {
        List<Shoes> shoesList = productService.getColorProducts(color);
        String shoes = new Gson().toJson(shoesList);
        return shoes;
    }

    @GetMapping("/kafka/producetest")
    public void produce(@RequestParam String message) {
        //control param to topic, Map<String, String>
        template.send("meat", message);
    }

    @GetMapping("/kafka/white_messages")
    public List<String> getWhiteMessages() {
        return myTopicConsumer.getWhite_messages();
    }

    @GetMapping("/kafka/white_recent")
    public Queue<String> getWhiteQueue(){
        return myTopicConsumer.getWhite_queue();
    }

    @GetMapping("/kafka/black_messages")
    public List<String> getBlackMessages() {
        return myTopicConsumer.getBlack_messages();
    }

    @GetMapping("/kafka/black_recent")
    public Queue<String> getBlackQueue(){
        return myTopicConsumer.getBlack_queue();
    }

    @Autowired
    ProductService productService;

    @RequestMapping("/getProduct")
    @ResponseBody
    public List<Shoes> getProductList() {
        return productService.getAllProducts();
    }
}
