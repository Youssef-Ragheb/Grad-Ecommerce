package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "active_ingredient")
public class ActiveIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // ✅ Added constraint
    private String activeIngredientName;

    private String ingredientArabicName;
    private String description;

    @OneToMany(mappedBy = "activeIngredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Drugs> drugs;
}
