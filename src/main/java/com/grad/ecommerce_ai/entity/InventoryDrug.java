package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "inventory_drugs")
public class InventoryDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drug_id", nullable = false)
    private Drugs drug;

    private float price;
    private int stock;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

}
