package com.croco.interview.management.user.repository;

import com.croco.interview.management.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    User findUserByIdentifier(String identifier);

    Page<User> findAll(Pageable pageable);
}
