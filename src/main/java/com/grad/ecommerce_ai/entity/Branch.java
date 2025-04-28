package com.grad.ecommerce_ai.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name = "branches", indexes = {
        @Index(name = "idx_branch_name", columnList = "branchName"),
        @Index(name = "idx_branch_email", columnList = "email"),
        @Index(name = "idx_branch_city", columnList = "city"),
        @Index(name = "idx_branch_phone", columnList = "phone")
})
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @Size(max = 255)
    @NotBlank
    private String branchName;

    @Size(max = 255)
    @NotBlank
    private String address;

    @Size(max = 255)
    @NotBlank
    private String city;

    @Size(max = 255)
    @NotBlank
    private String phone;

    @Size(max = 255)
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    private boolean branchState;

    private String zip;
    private double lat;
    private double lng;

    @ManyToOne(cascade = CascadeType.MERGE) // ✅ Changed from ALL to MERGE to prevent company deletion
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIgnore
    private Company company;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeDetails> employees;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryDrug> inventoryDrugs;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests; // ✅ Added orphanRemoval

    @Override
    public String toString() {
        return branchName;
    }

    public boolean getBranchState() {
        return branchState;
    }
}
