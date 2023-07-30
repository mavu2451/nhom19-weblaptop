package com.example.product.controller;

import com.example.product.entity.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RefreshScope
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping("/addProducts")
    public List<Product> addProducts(@RequestBody List<Product> productList){
        return productService.addProducts(productList);
    }

    @GetMapping("/getProducts")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/getProducts/{IdsList}")
    public List<Product> getProductsByIds(@PathVariable List<Long> IdsList){
        return productService.getProductsByIds(IdsList);
    }
}
