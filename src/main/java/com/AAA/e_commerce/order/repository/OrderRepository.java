package com.AAA.e_commerce.order.repository;


import com.AAA.e_commerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
