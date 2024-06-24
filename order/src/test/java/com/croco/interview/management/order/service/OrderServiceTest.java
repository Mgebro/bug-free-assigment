package com.croco.interview.management.order.service;

import com.croco.interview.management.order.model.entity.Order;
import com.croco.interview.management.order.model.entity.User;
import com.croco.interview.management.order.model.enums.Status;
import com.croco.interview.management.order.model.request.CreateOrderRequest;
import com.croco.interview.management.order.model.request.UpdateOrderRequest;
import com.croco.interview.management.order.model.response.OrderResponse;
import com.croco.interview.management.order.model.response.OrdersResponse;
import com.croco.interview.management.order.model.response.PageableResponse;
import com.croco.interview.management.order.repository.OrderRepository;
import com.croco.interview.management.order.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<Order> captor;

    @NotNull
    static User buildUser() {
        return User.builder()
                .withIdentifier("identifier")
                .withId(1L)
                .build();
    }

    @NotNull
    private static Order buildOrder() {
        return Order.builder()
                .withId(1L)
                .withProduct("product")
                .withQuantity("2")
                .withPrice(new BigDecimal("60.0"))
                .withStatus(Status.ACTIVE)
                .withUser(buildUser())
                .build();
    }

    @Test
    void testCreateOrder() {
        User user = buildUser();

        when(userRepository.findUserByIdentifier(user.getIdentifier())).thenReturn(user);

        orderService.createOrder(user.getIdentifier(), new CreateOrderRequest("product", "2", new BigDecimal("50.0")));

        verify(orderRepository).save(captor.capture());
        Order order = captor.getValue();

        assertEquals("product", order.getProduct());
        assertEquals("2", order.getQuantity());
        assertEquals(new BigDecimal("50.0"), order.getPrice());
        assertEquals("identifier", order.getUser().getIdentifier());
    }

    @Test
    void testGetOrder() {
        Order order = buildOrder();
        when(orderRepository.findOrderById(order.getId())).thenReturn(order);

        OrderResponse response = orderService.getOrder(order.getId());

        assertEquals("product", response.product());
        assertEquals("2", response.quantity());
        assertEquals(new BigDecimal("60.0"), response.price());
        assertEquals("identifier", response.user().userIdentifier());
    }

    @Test
    void testGetOrders() {
        Page<Order> mockPage = mock(Page.class);

        when(orderRepository.findAllByStatusIn(any(PageRequest.class), anyList())).thenReturn(mockPage);
        when(mockPage.getPageable()).thenReturn(PageRequest.of(0, 20));
        when(mockPage.getTotalElements()).thenReturn(0L);
        when(mockPage.getContent()).thenReturn(List.of());

        PageableResponse<OrdersResponse> response = orderService.getOrders(0, 20, Optional.of(List.of(Status.ACTIVE, Status.DELETED)));

        assertNotNull(response);
        assertEquals(0, response.page());
        assertEquals(20, response.size());
        assertEquals(0, response.total());
        assertEquals(new ArrayList<>(), response.content());
        verify(orderRepository).findAllByStatusIn(any(PageRequest.class), anyList());
    }

    @Test
    void testUpdateOrder() {
        Order order = buildOrder();

        when(orderRepository.findOrderById(order.getId())).thenReturn(order);

        orderService.updateOrder(order.getId(), new UpdateOrderRequest(
                "product2",
                "3",
                new BigDecimal("70.0")
        ));

        verify(orderRepository).save(captor.capture());
        Order capturedOrder = captor.getValue();

        assertEquals("product2", order.getProduct());
        assertEquals("3", order.getQuantity());
        assertEquals(new BigDecimal("70.0"), order.getPrice());
    }

    @Test
    void testDeleteOrder() {
        Order order = buildOrder();

        when(orderRepository.findOrderById(order.getId())).thenReturn(order);

        orderService.deleteOrder(order.getId());

        verify(orderRepository).save(captor.capture());
        Order capturedOrder = captor.getValue();

        assertEquals(Status.DELETED, capturedOrder.getStatus());
    }
}

