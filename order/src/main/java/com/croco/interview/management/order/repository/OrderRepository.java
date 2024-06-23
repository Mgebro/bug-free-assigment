package com.croco.interview.management.order.repository;

import com.croco.interview.management.order.model.entity.Order;
import com.croco.interview.management.order.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderById(Long id);

    Page<Order> findAllByStatusIn(Pageable pageable, List<Status> statuses);
}
