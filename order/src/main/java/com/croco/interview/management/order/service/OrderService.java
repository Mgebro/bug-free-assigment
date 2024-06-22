package com.croco.interview.management.order.service;

import com.croco.interview.management.order.model.entity.Order;
import com.croco.interview.management.order.model.entity.User;
import com.croco.interview.management.order.model.enums.Status;
import com.croco.interview.management.order.model.request.CreateOrderRequest;
import com.croco.interview.management.order.model.request.UpdateOrderRequest;
import com.croco.interview.management.order.model.response.OrderResponse;
import com.croco.interview.management.order.model.response.OrdersResponse;
import com.croco.interview.management.order.model.response.PageableResponse;
import com.croco.interview.management.order.model.response.UserResponse;
import com.croco.interview.management.order.repository.OrderRepository;
import com.croco.interview.management.order.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private <T> void updateField(Consumer<T> setter, @Nullable T value) {
        if (setter != null) {
            setter.accept(value);
        }
    }

    public void createOrder(String identifier, CreateOrderRequest request) {
        final User user = Objects.requireNonNull(userRepository.findUserByIdentifier(identifier), "User with identifier [%s] not found!".formatted(identifier));
        final Order order = Order.builder()
                .withProduct(request.product())
                .withQuantity(request.quantity())
                .withPrice(request.price())
                .withUser(user)
                .build();
        orderRepository.save(order);
    }

    public OrderResponse getOrder(Long id) {
        final Order order = Objects.requireNonNull(orderRepository.findOrderById(id), "Order with id [%s] not found!".formatted(id));
        return OrderResponse.builder()
                .withProduct(order.getProduct())
                .withQuantity(order.getQuantity())
                .withPrice(order.getPrice())
                .withUser(
                        UserResponse.builder()
                                .withUserIdentifier(
                                        order.getUser().getIdentifier()
                                )
                                .build()
                )
                .withStatus(order.getStatus())
                .build();
    }

    public PageableResponse<OrdersResponse> getOrders(Optional<Integer> page, Optional<Integer> size, Optional<List<Status>> statuses) {
        final Page<Order> result = orderRepository.findAllByStatusIn(
                PageRequest.of(
                        page.orElse(0),
                        size.orElse(20)
                ),
                statuses.orElse(
                        List.of(
                                Status.ACTIVE,
                                Status.DELETED
                        )
                )
        );

        return new PageableResponse<>(
                result.getPageable().getPageNumber(),
                result.getPageable().getPageSize(),
                result.getTotalElements(),
                result.getContent()
                        .stream()
                        .map(order -> OrdersResponse.builder()
                                .withId(order.getId())
                                .withProduct(order.getProduct())
                                .withQuantity(order.getQuantity())
                                .withPrice(order.getPrice())
                                .withUser(
                                        UserResponse.builder()
                                                .withUserIdentifier(order.getUser().getIdentifier())
                                                .build()
                                )
                                .withStatus(order.getStatus())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    public void updateOrder(Long id, UpdateOrderRequest request) {
        final Order order = Objects.requireNonNull(orderRepository.findOrderById(id), "Order with id [%s] not found!".formatted(id));
        updateField(order::setProduct, request.product());
        updateField(order::setQuantity, request.quantity());
        updateField(order::setPrice, request.price());
        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        final Order order = Objects.requireNonNull(orderRepository.findOrderById(id), "Order with id [%s] not found!".formatted(id));
        updateField(order::setStatus, Status.DELETED);
        orderRepository.save(order);
    }
}
