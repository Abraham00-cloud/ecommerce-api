package com.AAA.e_commerce.order.service;

import com.AAA.e_commerce.cart.dto.response.CartResponseDto;
import com.AAA.e_commerce.cart.model.Cart;
import com.AAA.e_commerce.cart.model.CartItem;
import com.AAA.e_commerce.cart.repository.CartRepository;
import com.AAA.e_commerce.cart.service.CartService;
import com.AAA.e_commerce.order.dto.response.OrderResponseDto;
import com.AAA.e_commerce.order.mapper.OrderMapper;
import com.AAA.e_commerce.order.model.Order;
import com.AAA.e_commerce.order.model.OrderItem;
import com.AAA.e_commerce.order.model.OrderStatus;
import com.AAA.e_commerce.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderMapper mapper;
    private final CartRepository cartRepository;
    private final OrderRepository repository;
    private final CartService cartService;
    public OrderResponseDto createOrder(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTime(LocalDateTime.now());

        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        BigDecimal total = orderItems
                .stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order savedOrder = repository.save(order);
        cartService.clearCart(cartId);
        return mapper.toOrderResponseDto(savedOrder);
    }

    public List<OrderResponseDto> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponseDto)
                .toList();
    }

    public OrderResponseDto getOrder(Long orderId) {
        return mapper.toOrderResponseDto(repository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")));
    }

    public OrderResponseDto updateStatus(Long orderId, OrderStatus status) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setOrderStatus(status);
        Order updateOrder = repository.save(order);
        return mapper.toOrderResponseDto(order);

    }
    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (order.getOrderStatus() == OrderStatus.SHIPPED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot cancel Shipped or Delivered order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = repository.save(order);
        return mapper.toOrderResponseDto(cancelledOrder);
    }


}
