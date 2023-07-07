package com.example.shopproject.controller;

import com.example.shopproject.model.Products;
import com.example.shopproject.service.ProductService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0", description = "Thêm thông tin sản phẩm"))
public class ProductRest {
    private final ProductService service;

    public ProductRest(ProductService service) {
        this.service = service;
    }
    @RequestMapping("/shop")
    public List<Products> findAll(){
        return service.listAll();
    }

    @PostMapping("/shop/new")
    public Products createProduct(@RequestBody Products p){
        return service.save(p);
    }

    @GetMapping("/find/{id}")
    public Products findProduct(@PathVariable long id){
        return service.getId(id);
    }
    @PutMapping("update/{id}")
    public Products updateProduct(@RequestBody Products p){
        return service.updateProduct(p);
    }
    @DeleteMapping("delete/{id}")
    public String deleteProduct(@PathVariable long id){
        return service.delete(id);
    }
}
