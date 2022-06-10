package com.cs237.kafkajava.controller;

import org.apache.kafka.common.protocol.types.Field;

import java.util.List;

public interface ProductService {
    public List<Shoes> getAllProducts();

    public void updateProduct(String id);

    public void insertProduct(Shoes shoe);

    public void replaceProduct(Shoes shoe);

    public List<Shoes> getColorProducts(String colors);
}
