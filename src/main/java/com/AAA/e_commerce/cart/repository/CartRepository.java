package com.AAA.e_commerce.cart.repository;

import com.AAA.e_commerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
