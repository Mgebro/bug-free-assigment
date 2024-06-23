package com.croco.interview.management.user.model.entity;

import com.croco.interview.management.user.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder(setterPrefix = "with")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(unique = true)
    private String identifier;

    private String firstName;

    private String lastName;

    private String password;

    @UpdateTimestamp
    private Instant updatedAt;

    @CreationTimestamp
    private Instant createdAt;
}
