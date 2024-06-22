package com.croco.interview.management.order.repository;

import com.croco.interview.management.order.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByIdentifier(String identifier);
}
