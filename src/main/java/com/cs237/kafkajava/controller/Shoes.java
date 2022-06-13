package com.cs237.kafkajava.controller;

import com.google.gson.Gson;
import com.opencsv.bean.CsvBindByName;
import org.apache.kafka.common.protocol.types.Field;

import java.io.Serializable;

public class Shoes implements Serializable {
        @CsvBindByName(column = "id")
        private String id;

        @CsvBindByName(column = "brand")
        private String brand;

        @CsvBindByName(column = "categories")
        private String categories;

        @CsvBindByName(column = "colors")
        private String colors;

//        @CsvBindByName(column = "imageURLs")
//        private String image_url;

        @CsvBindByName(column = "name")
        private String name;

        @CsvBindByName(column = "price")
        private Float price;

        @CsvBindByName(column = "quantity")
        private int quantity;

        @CsvBindByName(column = "lati")
        private String lati;

        @CsvBindByName(column = "longti")
        private String longti;
        //  getters, setters, toString

        @CsvBindByName(column = "producetime")
        private Long producetime;

        @CsvBindByName(column = "consumetime")
        private Long consumetime;

        public Object get(String key){
                return switch (key) {
                        case "name" -> name;
                        case "id" -> id;
                        case "brand" -> brand;
                        case "categories" -> categories;
                        case "colors" -> colors;
//                        case "image_url" -> image_url;
                        case "price" -> price;
                        case "quantity" -> quantity;
                        case "lati" -> lati;
                        case "longti" -> longti;
                        default -> name;
                };
        }

        public void set(String key, String value){
                switch (key) {
                        case "name" -> name = value;
                        case "id" -> id = value;
                        case "brand" -> brand = value;
                        case "categories" -> categories = value;
                        case "colors" -> colors = value;
//                        case "image_url" -> image_url = value;
                        case "price" -> price = Float.parseFloat(value);
                        case "quantity" -> quantity = Integer.parseInt(value);
                        case "lati" -> lati = value;
                        case "longti" -> longti = value;
                        case "producetime" -> producetime = Long.parseLong(value);
                        case "consumetime" -> consumetime = Long.parseLong(value);
                };
        }

        public String toString(){
                return id + "," + brand + "," + categories + "," + colors + "," + price + "," + quantity
                        + "," + lati + "," + longti;
        }

        public String getId() {
                return id;
        }

        public String getBrand() {
                return brand;
        }

        public String getCategories() {
                return categories;
        }

        public String getColors() {
                return colors;
        }

//        public String getImage_url() {
//                return image_url;
//        }

        public String getName() {
                return name;
        }

        public Float getPrice() {
                return price;
        }

        public int getQuantity() {
                return quantity;
        }

        public String getGeo_lat() {
                return lati;
        }

        public String getGeo_long() {
                return longti;
        }

        public Long getProduce_time(){return producetime;}

        public Long getConsume_time(){return consumetime;}
}
