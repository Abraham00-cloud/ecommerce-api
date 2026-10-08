package com.AAA.e_commerce.order.service;

import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.order.model.OrderItem;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.order.repository.OrderRepository;
import com.AAA.e_commerce.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupScheduler {
    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    public void releaseExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();

        List<Order> expiredOrders = orderRepository
                .findByOrderStatusAndExpiresAtBefore(OrderStatus.PENDING, now);

        if (expiredOrders.isEmpty()) {
            return;
        }

        log.info("Found {} expired pending orders to cancel.", expiredOrders.size());

        for (Order order : expiredOrders) {
            order.setOrderStatus(OrderStatus.EXPIRED);

            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
            }

            orderRepository.save(order);
            log.info("Order ID {} expired. Restored stock for items.", order.getId());
        }
    }
}
