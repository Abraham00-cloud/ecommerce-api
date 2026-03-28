package com.AAA.e_commerce.cart.model;

import com.AAA.e_commerce.product.model.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void updateQuantityAndPrice(int additionalQuantity, BigDecimal currentPrice) {
        this.quantity += additionalQuantity;
        this.unitPrice = currentPrice;
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
