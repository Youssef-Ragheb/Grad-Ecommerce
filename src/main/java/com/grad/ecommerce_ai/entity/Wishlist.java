package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
        name = "wishlist",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "drug_id"})} // Prevent duplicates
)
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drugs drug;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
