package com.AAA.e_commerce.cart.repository;

import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.cart.model.CartItem;
import com.AAA.e_commerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
CartItem findByCartAndProduct(Cart cart, Product product);
}
