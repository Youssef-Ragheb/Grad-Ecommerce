package com.grad.ecommerce_ai.entity.details;

import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.User;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "employee_details")
public class EmployeeDetails {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;


}
