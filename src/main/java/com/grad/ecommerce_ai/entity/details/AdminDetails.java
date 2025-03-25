package com.grad.ecommerce_ai.entity.details;

import com.grad.ecommerce_ai.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "admin_details")
public class AdminDetails {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    private User user;

    private String permissions;

}
