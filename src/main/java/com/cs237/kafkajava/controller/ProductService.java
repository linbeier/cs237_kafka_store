package com.cs237.kafkajava.controller;

import com.cs237.kafkajava.consumer.Product;

import java.util.List;

public interface ProductService {
    List<Shoes> getAllProducts();

    public void updateProduct(String id);
}
