package com.grad.ecommerce_ai.entity;

import com.grad.ecommerce_ai.dto.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "requests") // Changed to plural for consistency
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch; // Fixed variable naming

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Fixed column name

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer; // Fixed column name

    @Enumerated(EnumType.STRING)
    private Status status; // Stores Enum values as strings

    private float totalPriceOfRequest;
}
