package com.AAA.e_commerce.cart.model;

import com.AAA.e_commerce.product.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne()
    @JoinColumn(name = "Product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "Cart_id")
//    @JsonIgnore
    private Cart cart;
}
