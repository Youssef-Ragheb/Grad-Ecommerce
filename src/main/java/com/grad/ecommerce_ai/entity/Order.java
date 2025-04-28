package com.grad.ecommerce_ai.entity;

import com.grad.ecommerce_ai.dto.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float totalPrice;
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Fixed column name
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Request> requests;

    @ManyToOne
    @JoinColumn(name = "log_history_id") // Fixed column name
    private LogHistory logHistory;
    @CreatedDate
    private Timestamp createdAt;
}
