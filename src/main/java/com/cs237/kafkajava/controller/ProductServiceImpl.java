package com.cs237.kafkajava.controller;

import com.cs237.kafkajava.configuration.RedisConstant;
import com.cs237.kafkajava.consumer.Product;
import com.cs237.kafkajava.controller.ProductService;
import com.cs237.kafkajava.mapper.ProductMapper;
import com.cs237.kafkajava.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductMapper productMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Shoes> getAllProducts() {
        List<Shoes> products = (List<Shoes>) redisUtil.get(RedisConstant.ALL_PRODUCT_KEY);
        if(CollectionUtils.isEmpty(products)){
            products = productMapper.getAllProducts();
            redisUtil.set(RedisConstant.ALL_PRODUCT_KEY, products);
        }
        return products;
    }

    @Override
    @Transactional
    public void updateProduct(String id) {
        redisUtil.del(RedisConstant.ALL_PRODUCT_KEY);
        productMapper.updateProduct("abcd");
        productMapper.updateProduct("efgi");
    }
}