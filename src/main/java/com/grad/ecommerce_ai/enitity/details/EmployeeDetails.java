package com.grad.ecommerce_ai.enitity.details;

import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.User;
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
