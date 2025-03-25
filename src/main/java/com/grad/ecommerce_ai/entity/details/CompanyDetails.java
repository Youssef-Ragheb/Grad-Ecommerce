package com.grad.ecommerce_ai.entity.details;

import com.grad.ecommerce_ai.entity.Company;
import com.grad.ecommerce_ai.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company_details")
public class CompanyDetails {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private Company company;
}
