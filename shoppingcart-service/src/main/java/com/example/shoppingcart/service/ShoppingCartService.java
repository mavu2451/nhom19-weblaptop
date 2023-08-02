package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.CartEntity;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.ShoppingCartRequest;
import com.example.shoppingcart.model.ShoppingCartResponse;
import com.example.shoppingcart.repository.CartRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartService {
    @Autowired
    @Qualifier("webclient")
    private WebClient.Builder webBuilder;
    private final KafkaTemplate<String, Long> kafkaTemplate;
    @Autowired
    private CartRepository cartRepository;



    public ShoppingCartResponse processAddRequest(Long userId, List<ShoppingCartRequest> shoppingCartRequestList) {
        // call product-service
        ObjectMapper mapper = new ObjectMapper();
        String productServiceUrl = "http://product-service/products/getProducts/" + shoppingCartRequestList.stream().map(e -> String.valueOf(e.getProductId())).collect(Collectors.joining(","));
        List<Product> productServiceList = webBuilder.build()
                .get()
                .uri(productServiceUrl)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();

        System.out.println(productServiceUrl);
        System.out.println("productServiceList -> "+productServiceList);
        // calculate totalcost
        final Double[] totalCost = {0.0};
        productServiceList.forEach(psl -> {
            shoppingCartRequestList.forEach(scr -> {
                if (psl.getProductId() == scr.getProductId()) {
                    psl.setQuantity(scr.getQuantity());
                    totalCost[0] = totalCost[0] + psl.getAmount() * scr.getQuantity();
                }
            });
        });

        // create CartEntity
        CartEntity cartEntity = null;
        try {
            cartEntity = CartEntity.builder()
                    .userId(userId)
                    .cartId((long) (Math.random() * Math.pow(10, 10)))
                    .totalItems(productServiceList.size())
                    .totalCost(totalCost[0])
                    .products(mapper.writeValueAsString(productServiceList))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // save CartEntity
        cartEntity = cartRepository.save(cartEntity);

        // create api response
        ShoppingCartResponse response = ShoppingCartResponse.builder()
                .cartId(cartEntity.getCartId())
                .userId(cartEntity.getUserId())
                .totalItems(cartEntity.getTotalItems())
                .totalCost(cartEntity.getTotalCost())
                .products(productServiceList)
                .build();
        kafkaTemplate.send("notifications", response.getCartId());
        return response;
    }

    public List<ShoppingCartResponse> getShoppingCart(Long userId) {
        ObjectMapper mapper = new ObjectMapper();
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        List<ShoppingCartResponse> cartResponse = cartEntities.stream()
                .map(ce -> {
                    try {
                        return ShoppingCartResponse.builder()
                                .cartId(ce.getCartId())
                                .userId(ce.getUserId())
                                .totalItems(ce.getTotalItems())
                                .totalCost(ce.getTotalCost())
                                .products(mapper.readValue(ce.getProducts(), List.class))
                                .build();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        return cartResponse;
    }
}
