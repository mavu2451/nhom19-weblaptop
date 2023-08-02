package com.example.product.controller;

import com.example.product.entity.Product;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@RefreshScope

public class ProductController {
    @Autowired
    private ProductService productService;


    @RouterOperation(operation = @Operation(summary = "Thêm sản phẩm", operationId = "addProducts"))
    @PostMapping("/addProducts")
    public List<Product> addProducts(@RequestBody List<Product> productList){
        return productService.addProducts(productList);
    }
    @RouterOperation(operation = @Operation(summary = "Lấy thông tin sản phẩm", operationId = "getProducts"))
    @GetMapping("/getProducts")
    public List<Product> getProducts(){
        return productService.getProducts();
    }
    @RouterOperation(operation = @Operation(summary = "Lấy thông tin từng sản phẩm", operationId = "getProductsByIds"))
    @GetMapping("/getProducts/{IdsList}")
    public List<Product> getProductsByIds(@PathVariable List<Long> IdsList){
        return productService.getProductsByIds(IdsList);
    }
}
