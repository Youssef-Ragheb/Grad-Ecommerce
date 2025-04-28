package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drugs drug;

    private float price;

    @NotNull
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request; // Added request relation

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
