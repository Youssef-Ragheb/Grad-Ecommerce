package com.grad.ecommerce_ai.enitity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "company", indexes = {
        @Index(name = "idx_company_name", columnList = "name"),
        @Index(name = "idx_company_email", columnList = "companyEmail"),
        @Index(name = "idx_company_phone", columnList = "phone")
})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    @Size(max = 255)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    @Size(max = 255)
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String companyEmail;
    @Column(nullable = false, unique = true)
    private String phone;
    public String logoUrl;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("company")
    @ToString.Exclude
    private List<Branch> branchList;
    @Override
    public String toString() {
        return name +" "+ companyEmail;
    }
}