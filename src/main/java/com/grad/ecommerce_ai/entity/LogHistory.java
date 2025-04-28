package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "log_history")
public class LogHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "logHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders; // Fixed mappedBy name

    @OneToOne
    @JoinColumn(name = "user_id") // Ensures correct foreign key reference
    private User user;
}
