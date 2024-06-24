package com.croco.interview.management.order.controller;

import com.croco.interview.management.order.model.enums.Status;
import com.croco.interview.management.order.model.request.CreateOrderRequest;
import com.croco.interview.management.order.model.request.UpdateOrderRequest;
import com.croco.interview.management.order.model.response.OrderResponse;
import com.croco.interview.management.order.model.response.OrdersResponse;
import com.croco.interview.management.order.model.response.PageableResponse;
import com.croco.interview.management.order.model.response.UserResponse;
import com.croco.interview.management.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testCreateOrder() throws Exception {
        String identifier = "identifier";
        CreateOrderRequest request = new CreateOrderRequest(
                "product1",
                "2",
                new BigDecimal(50));

        when(principal.getName()).thenReturn("identifier");

        mockMvc.perform(post("/api/order")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        verify(orderService, times(1)).createOrder(identifier, request);
    }

    @Test
    @WithMockUser(username = "identifier")
    void testGetOrder() throws Exception {
        Long orderId = 1L;
        OrderResponse response = new OrderResponse("product1", "2", new BigDecimal("50.0"), new UserResponse("user1"), Status.ACTIVE);
        when(orderService.getOrder(orderId)).thenReturn(response);
        mockMvc.perform(get("/api/order/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("product1"));
    }

    @Test
    @WithMockUser(username = "identifier")
    void testGetOrders() throws Exception {
        PageableResponse<OrdersResponse> pageableResponse = new PageableResponse<>(0, 20, 0L, List.of());

        when(orderService.getOrders(0, 20, Optional.empty())).thenReturn(pageableResponse);

        mockMvc.perform(get("/api/order/all")
                        .param("page", "0")
                        .param("size", "20"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPartialContent());

        verify(orderService, times(1)).getOrders(anyInt(), anyInt(), any(Optional.class));
    }

    @Test
    @WithMockUser(username = "identifier")
    void testUpdateOrder() throws Exception {
        Long orderId = 1L;

        UpdateOrderRequest request = new UpdateOrderRequest("product2", "3", new BigDecimal("60.0"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).updateOrder(orderId, request);
    }

    @Test
    @WithMockUser(username = "identifier")
    void testDeleteOrder() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(delete("/api/order/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(orderId);
    }
}

