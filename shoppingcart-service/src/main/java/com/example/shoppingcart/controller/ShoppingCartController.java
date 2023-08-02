package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.ShoppingCartRequest;
import com.example.shoppingcart.model.ShoppingCartResponse;
import com.example.shoppingcart.service.ShoppingCartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {
    private static final String PRODUCT_SERVICE = "product";
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/{userId}/products")
    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "serviceAFallback")
    public ResponseEntity addProductsToCart(
            @PathVariable Long userId,
            @RequestBody List<ShoppingCartRequest> reqProductList
    ){
        ShoppingCartResponse response = shoppingCartService.processAddRequest(userId, reqProductList);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @GetMapping("/{userId}")
    public ResponseEntity getShoppingCart(@PathVariable Long userId){
        return ResponseEntity.ok(shoppingCartService.getShoppingCart(userId));
    }

    public ResponseEntity serviceAFallback(Exception e) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Máy chủ đã đóng, mời bạn quay lại sau!");
    }
}
