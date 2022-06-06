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

        @CsvBindByName(column = "imageURLs")
        private String image_url;

        @CsvBindByName(column = "name")
        private String name;

        @CsvBindByName(column = "Price")
        private Float price;

        @CsvBindByName(column = "Quantity")
        private int quantity;

        @CsvBindByName(column = "Geo-lat")
        private String geo_lat;

        @CsvBindByName(column = "Geo-long")
        private String geo_long;
        //  getters, setters, toString

        public Object get(String key){
                return switch (key) {
                        case "name" -> name;
                        case "id" -> id;
                        case "brand" -> brand;
                        case "categories" -> categories;
                        case "colors" -> colors;
                        case "image_url" -> image_url;
                        case "price" -> price;
                        case "quantity" -> quantity;
                        case "geo_lat" -> geo_lat;
                        case "geo_long" -> geo_long;
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
                        case "image_url" -> image_url = value;
                        case "price" -> price = Float.parseFloat(value);
                        case "quantity" -> quantity = Integer.parseInt(value);
                        case "geo_lat" -> geo_lat = value;
                        case "geo_long" -> geo_long = value;
                };
        }

        public String toString(){
                return id + "," + brand + "," + categories + "," + colors + "," + image_url + "," + price + "," + quantity
                        + "," + geo_lat + "," + geo_long;
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

        public String getImage_url() {
                return image_url;
        }

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
                return geo_lat;
        }

        public String getGeo_long() {
                return geo_long;
        }
}
