package com.example.shoppingcart.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class ShoppingCartResponse {
    private Long userId;
    private Long cartId;
    private Integer totalItems;
    private Double totalCost;
    private List<Product> products;
}
