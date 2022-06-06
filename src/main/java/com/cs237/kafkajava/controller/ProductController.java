package com.cs237.kafkajava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@Controller
//public class ProductController {
//
//    @Autowired
//    ProductService productService;
//
//    @RequestMapping("/getProduct")
//    @ResponseBody
//    public List<Product> getProductList() {
//        return productService.getAllProducts();
//    }
//
//    @RequestMapping("/update")
//    @ResponseBody
//    public int update() {
//        productService.updateProduct("new id");
//        return 0;
//    }
//}
