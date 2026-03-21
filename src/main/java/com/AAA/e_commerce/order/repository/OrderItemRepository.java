package com.AAA.e_commerce.order.repository;

import com.AAA.e_commerce.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
