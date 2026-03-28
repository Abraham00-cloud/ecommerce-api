package com.AAA.e_commerce.order.repository;

import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
