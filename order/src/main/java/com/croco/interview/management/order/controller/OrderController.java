package com.croco.interview.management.order.controller;

import com.croco.interview.management.order.model.enums.Status;
import com.croco.interview.management.order.model.request.CreateOrderRequest;
import com.croco.interview.management.order.model.request.UpdateOrderRequest;
import com.croco.interview.management.order.model.response.OrderResponse;
import com.croco.interview.management.order.model.response.OrdersResponse;
import com.croco.interview.management.order.model.response.PageableResponse;
import com.croco.interview.management.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createOrder(Principal principal, @RequestBody @Valid CreateOrderRequest request) {
        orderService.createOrder(principal.getName(), request);
    }

    @GetMapping("/{id}")
    OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    PageableResponse<OrdersResponse> getOrders(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<List<Status>> statuses
    ) {
        return orderService.getOrders(page.orElse(0), size.orElse(20), statuses);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateOrder(@PathVariable Long id, @RequestBody @Valid UpdateOrderRequest request) {
        orderService.updateOrder(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
