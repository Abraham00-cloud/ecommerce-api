package com.AAA.e_commerce.order.repository;

import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);

    Optional<Order> findByTransactionReference(String transactionReference);


    List<Order> findByOrderStatusAndExpiresAtBefore(OrderStatus orderStatus, LocalDateTime now);
}
