package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "drugs")
public class Drugs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "active_ingredient_id", nullable = false)
    private ActiveIngredient activeIngredient;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, unique = true)
    private String drugName; // ✅ Added constraints

    private String description;

    @Size(max = 1080)
    private String logo;

    @OneToMany(mappedBy = "drug", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InventoryDrug> inventoryDrugList; // ✅ Added orphanRemoval

    @OneToMany(mappedBy = "drug", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Item> itemList; // ✅ Added orphanRemoval
}
