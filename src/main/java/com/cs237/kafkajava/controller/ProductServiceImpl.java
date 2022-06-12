package com.cs237.kafkajava.controller;

import com.cs237.kafkajava.configuration.RedisConstant;
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

//    @Autowired
//    RedisUtil redisUtil;

    @Override
    public List<Shoes> getAllProducts() {
        // Problem: Don't update the redis cache.
//        List<Shoes> products = (List<Shoes>) redisUtil.get(RedisConstant.ALL_PRODUCT_KEY);
//        if(CollectionUtils.isEmpty(products)){
//            products = productMapper.getAllProducts();
//            redisUtil.set(RedisConstant.ALL_PRODUCT_KEY, products);
//        }
        List<Shoes> products = productMapper.getAllProducts();
        return products;
    }

    @Override
    @Transactional
    public void updateProduct(String id) {
//        redisUtil.del(RedisConstant.ALL_PRODUCT_KEY);
        productMapper.updateProduct("abcd");
        productMapper.updateProduct("efgi");
    }

    @Override
    @Transactional
    public void insertProduct(Shoes shoe) {
        productMapper.insertProduct(shoe);
    }

    @Override
    @Transactional
    public void replaceProduct(Shoes shoe) {
        productMapper.replaceProduct(shoe);
    }

    @Override
    @Transactional
    public List<Shoes> getColorProducts(String colors){
        List<Shoes> shoes = productMapper.getColorProducts(colors);
        return shoes;
    }
}
