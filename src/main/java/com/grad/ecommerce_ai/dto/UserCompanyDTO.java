package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Gender;
import com.grad.ecommerce_ai.enitity.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class UserCompanyDTO {

    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private OffsetDateTime dateCreated;
    private boolean enabled;
    private UserRoles userRoles;
    private Boolean companyRegistrationCompleted;
    @NotBlank
    private String companyName;
    @NotBlank
    @Email
    private String companyEmail;
    @NotBlank
    private String companyPhone;
    private String logoUrl;
    private List<Branch> branchList;
}
