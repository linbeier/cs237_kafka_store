package com.cs237.kafkajava.mapper;

import com.cs237.kafkajava.controller.Shoes;

import java.util.List;

public interface ProductMapper {
    List<Shoes> getAllProducts();

    int updateProduct(String id);

    void insertProduct(Shoes shoe);
}
