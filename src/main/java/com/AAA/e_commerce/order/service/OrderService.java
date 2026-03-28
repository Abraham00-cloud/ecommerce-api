package com.AAA.e_commerce.order.service;

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
import com.AAA.e_commerce.product.model.Product;
import com.AAA.e_commerce.user.model.Role;
import com.AAA.e_commerce.user.model.User;
import com.AAA.e_commerce.user.repository.UserRepository;
import com.AAA.e_commerce.user.service.UserService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderMapper mapper;
    private final CartRepository cartRepository;
    private final OrderRepository repository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public OrderResponseDto createOrder() {
        User user = userService.getAuthenticatedUser();
        Cart cart = user.getCart();
        if (cart.getCartItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTime(LocalDateTime.now());

        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Not enough stock for product;" + product.getName());
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        BigDecimal total =
                orderItems.stream()
                        .map(OrderItem::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order savedOrder = repository.save(order);
        cartService.clearCart();
        return mapper.toOrderResponseDto(savedOrder);
    }

    public List<OrderResponseDto> getAllOrders() {
        return repository.findAll().stream().map(mapper::toOrderResponseDto).toList();
    }

    public OrderResponseDto getOrder(Long orderId) {
        User currentUser = userService.getAuthenticatedUser();

        Order order =
                repository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have permission to view this order");
        }
        return mapper.toOrderResponseDto(order);
    }

    public List<OrderResponseDto> getMyOrders() {
        User currentUser = userService.getAuthenticatedUser();

        return repository.findByUser(currentUser).stream().map(mapper::toOrderResponseDto).toList();
    }

    public OrderResponseDto updateStatus(Long orderId, OrderStatus status) {
        Order order =
                repository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Order not found"));
        order.setOrderStatus(status);
        Order updateOrder = repository.save(order);
        return mapper.toOrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        User currentUser = userService.getAuthenticatedUser();
        Order order =
                repository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Order not found"));
        if (!order.getUser().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have permission to view this order");
        }
        if (order.getOrderStatus() == OrderStatus.SHIPPED
                || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "cannot cancel Shipped or Delivered order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
        }
        Order cancelledOrder = repository.save(order);
        return mapper.toOrderResponseDto(cancelledOrder);
    }
}
